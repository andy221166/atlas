<template>
  <div v-if="orderId" class="order-tracking card shadow-sm mt-3">
    <div class="card-body">
      <h6 class="card-title text-center text-primary mb-3">Order Tracking</h6>

      <div class="d-flex justify-content-between">
        <span class="text-muted">Order ID:</span>
        <span class="fw-bold">{{ orderId }}</span>
      </div>

      <!-- Short polling updates -->
      <div class="mt-4">
        <h6 class="text-secondary">Short Polling Updates</h6>
        <div class="d-flex justify-content-between mt-2">
          <span class="text-muted">Status:</span>
          <span :class="applyBadgeClass(shortPollingOrderStatus)">{{
            shortPollingOrderStatus
          }}</span>
        </div>
        <div v-if="shortPollingCanceledReason" class="mt-2 text-danger">
          <span class="text-muted">Cancellation Reason:</span>
          <p>{{ shortPollingCanceledReason }}</p>
        </div>
      </div>

      <!-- WebSocket updates -->
      <div class="mt-4">
        <h6 class="text-secondary">WebSocket Updates</h6>
        <div class="d-flex justify-content-between mt-2">
          <span class="text-muted">Status:</span>
          <span :class="applyBadgeClass(wsOrderStatus)">{{
            wsOrderStatus
          }}</span>
        </div>
        <div v-if="wsCanceledReason" class="mt-2 text-danger">
          <span class="text-muted">Cancellation Reason:</span>
          <p>{{ wsCanceledReason }}</p>
        </div>
      </div>

      <!-- SSE updates -->
      <div class="mt-4">
        <h6 class="text-secondary">SSE Updates</h6>
        <div class="d-flex justify-content-between mt-2">
          <span class="text-muted">Status:</span>
          <span :class="applyBadgeClass(sseOrderStatus)">{{
            sseOrderStatus
          }}</span>
        </div>
        <div v-if="sseCanceledReason" class="mt-2 text-danger">
          <span class="text-muted">Cancellation Reason:</span>
          <p>{{ sseCanceledReason }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {computed, onBeforeUnmount, onMounted, ref, watch} from "vue";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { api } from "@/api";
import {applyBadgeClass} from "@/utils/orderStatusBadgeClass";

const NOTIFICATION_BASE_URL = process.env.VUE_APP_NOTIFICATION_BASE_URL;

export default {
  props: {
    orderId: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const shortPollingOrderStatus = ref('PROCESSING');
    const shortPollingCanceledReason = ref(null);
    const wsOrderStatus = ref('PROCESSING');
    const wsCanceledReason = ref(null);
    const sseOrderStatus = ref('PROCESSING');
    const sseCanceledReason = ref(null);

    let pollingInterval = null;
    let stompClient = null;
    let eventSource = null;

    const startShortPolling = (orderId) => {
      const pollOrder = async () => {
        try {
          const { data } = await api.orders.getStatus(orderId);
          const { status, canceledReason } = data.data;

          shortPollingOrderStatus.value = status;
          shortPollingCanceledReason.value = canceledReason || null;

          if (status === "CONFIRMED" || status === "CANCELED") {
            stopShortPolling();
          }
        } catch (error) {
          console.error("Error fetching order:", error);
        }
      };

      pollingInterval = setInterval(pollOrder, 3000);
    };

    const stopShortPolling = () => {
      if (pollingInterval) {
        clearInterval(pollingInterval);
        pollingInterval = null;
      }
    };

    const connectWebSocket = (orderId) => {
      if (stompClient) stompClient.disconnect();

      const socket = new SockJS(`${api.notifications.getWebSocketUrl()}`);
      stompClient = Stomp.over(socket);
      
      stompClient.onStompError = (frame) => {
        console.error('WebSocket connection error:', frame);
      };

      stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/orders/${orderId}/status`, (message) => {
          const { orderStatus, canceledReason } = JSON.parse(message.body);
          wsOrderStatus.value = orderStatus;
          wsCanceledReason.value = canceledReason || null;          
        });
      });
    };

    const connectSSE = (orderId) => {
      if (eventSource) {
        eventSource.close();
      }

      eventSource = new EventSource(api.notifications.getSSEUrl(orderId));

      eventSource.onopen = () => {
        console.log('SSE connection established');
      };

      eventSource.onerror = (error) => {
        console.error('SSE connection error:', error);
        if (eventSource.readyState === EventSource.CLOSED) {
          console.log('SSE connection closed');
        }
      };

      eventSource.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          sseOrderStatus.value = data.orderStatus;
          sseCanceledReason.value = data.canceledReason || null;
        } catch (error) {
          console.error('Error parsing SSE message:', error);
        }
      };

      // Optional: Handle specific event types
      eventSource.addEventListener('orderStatus', (event) => {
        try {
          const data = JSON.parse(event.data);
          sseOrderStatus.value = data.orderStatus;
          sseCanceledReason.value = data.canceledReason || null;
        } catch (error) {
          console.error('Error parsing orderStatus event:', error);
        }
      });
    };

    const cleanup = () => {
      stopShortPolling();
      if (stompClient) {
        stompClient.disconnect();
        stompClient = null;
      }
      if (eventSource) {
        eventSource.close();
        eventSource = null;
      }
    };

    watch(() => props.orderId, (newOrderId) => {
      resetOrderTrackingInfo();
      if (newOrderId) {
        startShortPolling(newOrderId);
        connectWebSocket(newOrderId);
        connectSSE(newOrderId);
      } else {
        cleanup();
      }
    });

    onMounted(() => {
      if (props.orderId) {
        startShortPolling(props.orderId);
        connectWebSocket(props.orderId);
        connectSSE(props.orderId);
      }
    });

    onBeforeUnmount(cleanup);

    const resetOrderTrackingInfo = () => {
      shortPollingOrderStatus.value = "PROCESSING";
      wsOrderStatus.value = "PROCESSING";
      sseOrderStatus.value = "PROCESSING";
      shortPollingCanceledReason.value = null;
      wsCanceledReason.value = null;
      sseCanceledReason.value = null;
    };

    return {
      orderId: computed(() => props.orderId),
      shortPollingOrderStatus,
      shortPollingCanceledReason,
      wsOrderStatus,
      wsCanceledReason,
      sseOrderStatus,
      sseCanceledReason,
      applyBadgeClass,
    };
  },
};
</script>

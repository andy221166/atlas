<template>
  <div v-if="orderId" class="order-tracking card shadow-sm mt-3">
    <div class="card-body">
      <h6 class="card-title text-center text-primary mb-3">Order Tracking</h6>

      <div class="d-flex justify-content-between">
        <span class="text-muted">Order ID:</span>
        <span class="fw-bold">{{ orderId }}</span>
      </div>

      <!-- Long polling updates -->
      <div class="mt-4">
        <h6 class="text-secondary">Long Polling Updates</h6>
        <div class="d-flex justify-content-between mt-2">
          <span class="text-muted">Status:</span>
          <span :class="applyBadgeClass(longPollingOrderStatus)">{{
            longPollingOrderStatus
          }}</span>
        </div>
        <div v-if="longPollingCanceledReason" class="mt-2 text-danger">
          <span class="text-muted">Cancellation Reason:</span>
          <p>{{ longPollingCanceledReason }}</p>
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
    const longPollingOrderStatus = ref('PROCESSING');
    const longPollingCanceledReason = ref(null);
    const wsOrderStatus = ref('PROCESSING');
    const wsCanceledReason = ref(null);
    const sseOrderStatus = ref('PROCESSING');
    const sseCanceledReason = ref(null);

    let pollingInterval = null;
    let stompClient = null;
    let eventSource = null;

    const startLongPolling = (orderId) => {
      const pollOrder = async () => {
        try {
          const { data } = await api.orders.getStatus(orderId);
          const { status, canceledReason } = data.data;

          longPollingOrderStatus.value = status;
          longPollingCanceledReason.value = canceledReason || null;

          if (status === "CONFIRMED" || status === "CANCELED") {
            stopLongPolling();
          }
        } catch (error) {
          console.error("Error fetching order:", error);
        }
      };

      pollingInterval = setInterval(pollOrder, 3000);
    };

    const stopLongPolling = () => {
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
        startLongPolling(orderId);
      };

      stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/order/${orderId}`, (message) => {
          const { orderStatus, canceledReason } = JSON.parse(message.body);
          wsOrderStatus.value = orderStatus;
          wsCanceledReason.value = canceledReason || null;
          
          stopLongPolling();
        });
      });
    };

    const connectSSE = (orderId) => {
      if (eventSource) eventSource.close();

      eventSource = new EventSource(api.notifications.getSSEUrl(orderId));
      
      eventSource.onerror = (error) => {
        console.error('SSE connection error:', error);
        eventSource.close();
        startLongPolling(orderId);
      };

      eventSource.addEventListener("orderStatus", (event) => {
        const { orderStatus, canceledReason } = JSON.parse(event.data);
        sseOrderStatus.value = orderStatus;
        sseCanceledReason.value = canceledReason || null;
        
        stopLongPolling();
      });
    };

    const cleanup = () => {
      stopLongPolling();
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
        startLongPolling(newOrderId);
        connectWebSocket(newOrderId);
        connectSSE(newOrderId);
      } else {
        cleanup();
      }
    });

    onMounted(() => {
      if (props.orderId) {
        startLongPolling(props.orderId);
        connectWebSocket(props.orderId);
        connectSSE(props.orderId);
      }
    });

    onBeforeUnmount(cleanup);

    const resetOrderTrackingInfo = () => {
      longPollingOrderStatus.value = "PROCESSING";
      wsOrderStatus.value = "PROCESSING";
      sseOrderStatus.value = "PROCESSING";
      longPollingCanceledReason.value = null;
      wsCanceledReason.value = null;
      sseCanceledReason.value = null;
    };

    return {
      orderId: computed(() => props.orderId),
      longPollingOrderStatus,
      longPollingCanceledReason,
      wsOrderStatus,
      wsCanceledReason,
      sseOrderStatus,
      sseCanceledReason,
      applyBadgeClass,
    };
  },
};
</script>

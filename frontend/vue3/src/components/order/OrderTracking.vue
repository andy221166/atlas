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
          <span :class="applyBadgeClass(orderStatuses.shortPolling)">
            {{ orderStatuses.shortPolling }}
          </span>
        </div>
        <div v-if="canceledReasons.shortPolling" class="mt-2 text-danger">
          <span class="text-muted">Cancellation Reason:</span>
          <p>{{ canceledReasons.shortPolling }}</p>
        </div>
      </div>

      <!-- SSE updates -->
      <div class="mt-4">
        <h6 class="text-secondary">SSE Updates</h6>
        <div class="d-flex justify-content-between mt-2">
          <span class="text-muted">Status:</span>
          <span :class="applyBadgeClass(orderStatuses.sse)">
            {{ orderStatuses.sse }}
          </span>
        </div>
        <div v-if="canceledReasons.sse" class="mt-2 text-danger">
          <span class="text-muted">Cancellation Reason:</span>
          <p>{{ canceledReasons.sse }}</p>
        </div>
      </div>

      <!-- WebSocket updates -->
      <div class="mt-4">
        <h6 class="text-secondary">WebSocket Updates</h6>
        <div class="d-flex justify-content-between mt-2">
          <span class="text-muted">Status:</span>
          <span :class="applyBadgeClass(orderStatuses.ws)">
            {{ orderStatuses.ws }}
          </span>
        </div>
        <div v-if="canceledReasons.ws" class="mt-2 text-danger">
          <span class="text-muted">Cancellation Reason:</span>
          <p>{{ canceledReasons.ws }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {computed, onBeforeUnmount, onMounted, ref, watch} from "vue";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {api} from "@/api";
import {applyBadgeClass} from "@/utils/orderStatusBadgeClass";
import {toast} from "vue3-toastify";

export default {
  props: {
    orderId: {
      type: String,
      required: true
    }
  },
  setup(props) {
    // Create reactive objects using ref
    const orderStatuses = ref({
      shortPolling: 'PROCESSING',
      sse: 'PROCESSING',
      ws: 'PROCESSING'
    });

    const canceledReasons = ref({
      shortPolling: null,
      sse: null,
      ws: null
    });

    let pollingInterval = null;
    let stompClient = null;
    let eventSource = null;

    const updateOrderStatus = (type, status, reason) => {
      orderStatuses.value[type] = status;
      canceledReasons.value[type] = reason || null;
    };

    const startShortPolling = (orderId) => {
      const pollOrder = async () => {
        try {
          const { data } = await api.orders.getStatus(orderId);
          if (data.success) {
            const { status, canceledReason } = data.data;
            updateOrderStatus('shortPolling', status, canceledReason);
            if (status === "CONFIRMED" || status === "CANCELED") {
              stopShortPolling();
            }
          } else {
            toast.error(data.message);
          }
        } catch (error) {
          toast.error("Error fetching order:", error);
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

    const connectSSE = (orderId) => {
      if (eventSource) eventSource.close();

      eventSource = new EventSource(api.notifications.getSSEUrl(orderId));

      eventSource.addEventListener('open', (event) => {
        console.log('SSE connection established', event);
      });

      // Listen specifically for ORDER_STATUS_CHANGED events
      eventSource.addEventListener('ORDER_STATUS_CHANGED', (event) => {
        console.log('SSE received ORDER_STATUS_CHANGED event:', event.data);
        try {
          const data = JSON.parse(event.data);
          updateOrderStatus('sse', data.orderStatus, data.canceledReason);
        } catch (error) {
          console.error('SSE error parsing message: ' + error.message);
        }
      });

      eventSource.addEventListener('error', (error) => {
        console.error('SSE connection error:', error);
        if (eventSource.readyState === EventSource.CLOSED) {
          console.log('SSE connection closed');
        }
      });
    };

    const connectWebSocket = (orderId) => {
      if (stompClient) stompClient.disconnect();

      const socket = new SockJS(api.notifications.getWebSocketUrl());
      stompClient = Stomp.over(socket);

      stompClient.onStompError = (frame) => {
        console.error('WebSocket connection error:', frame);
      };

      stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/orders/${orderId}/status`, (message) => {
          const { orderStatus, canceledReason } = JSON.parse(message.body);
          updateOrderStatus('ws', orderStatus, canceledReason);
        });
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

    const resetOrderTrackingInfo = () => {
      Object.keys(orderStatuses.value).forEach(key => {
        orderStatuses.value[key] = "PROCESSING";
      });
      Object.keys(canceledReasons.value).forEach(key => {
        canceledReasons.value[key] = null;
      });
    };

    watch(() => props.orderId, (newOrderId) => {
      resetOrderTrackingInfo();
      if (newOrderId) {
        startShortPolling(newOrderId);
        connectSSE(newOrderId);
        connectWebSocket(newOrderId);
      } else {
        cleanup();
      }
    });

    onMounted(() => {
      if (props.orderId) {
        startShortPolling(props.orderId);
        connectSSE(props.orderId);
        connectWebSocket(props.orderId);
      }
    });

    onBeforeUnmount(cleanup);

    return {
      orderId: computed(() => props.orderId),
      orderStatuses,
      canceledReasons,
      applyBadgeClass,
    };
  },
};
</script>

<template>
  <div v-if="orderId" class="orderPayload-tracking card shadow-sm mt-3">
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
import {useStore} from "vuex";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {getOrderApi} from "@/api/orderPayload";
import {applyBadgeClass} from "@/utils/orderStatusBadgeClass";

const NOTIFICATION_BASE_URL = process.env.VUE_APP_NOTIFICATION_BASE_URL;

export default {
  setup() {
    const store = useStore();
    const orderId = computed(() => store.state.orderTracking.orderId);

    const longPollingOrderStatus = ref(store.state.orderTracking.status);
    const longPollingCanceledReason = ref(
      store.state.orderTracking.canceledReason
    );
    const wsOrderStatus = ref(store.state.orderTracking.status);
    const wsCanceledReason = ref(store.state.orderTracking.canceledReason);
    const sseOrderStatus = ref(store.state.orderTracking.status);
    const sseCanceledReason = ref(store.state.orderTracking.canceledReason);

    let pollingInterval = null;
    let stompClient = null;
    let eventSource = null;

    const startLongPolling = (orderId) => {
      const pollOrder = async () => {
        try {
          const response = await getOrderApi(orderId);
          const { status, canceledReason } = response.data.data;

          longPollingOrderStatus.value = status;
          longPollingCanceledReason.value = canceledReason || null;

          if (status === "CONFIRMED" || status === "CANCELED") {
            stopLongPolling();
          }
        } catch (error) {
          console.error("Error fetching orderPayload:", error);
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

      const socket = new SockJS(`${NOTIFICATION_BASE_URL}/notification/ws`);
      stompClient = Stomp.over(socket);
      stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/orderPayload/${orderId}`, (message) => {
          const { orderStatus, canceledReason } = JSON.parse(message.body);
          wsOrderStatus.value = orderStatus;
          wsCanceledReason.value = canceledReason || null;
        });
      });
    };

    const connectSSE = (orderId) => {
      if (eventSource) eventSource.close();

      eventSource = new EventSource(
        `${NOTIFICATION_BASE_URL}/notification/sse/orders/${orderId}/status`
      );
      eventSource.addEventListener("orderStatus", (event) => {
        const { orderStatus, canceledReason } = JSON.parse(event.data);
        sseOrderStatus.value = orderStatus;
        sseCanceledReason.value = canceledReason || null;
      });
      eventSource.onerror = () => {
        eventSource.close();
      };
    };

    watch(orderId, (newOrderId) => {
      resetOrderTrackingInfo();
      if (newOrderId) {
        startLongPolling(newOrderId);
        connectWebSocket(newOrderId);
        connectSSE(newOrderId);
      } else {
        stopLongPolling();
        if (stompClient) stompClient.disconnect();
        if (eventSource) eventSource.close();
      }
    });

    onMounted(() => {
      if (orderId.value) {
        startLongPolling(orderId.value);
        connectWebSocket(orderId.value);
        connectSSE(orderId.value);
      }
    });

    onBeforeUnmount(() => {
      stopLongPolling();
      if (stompClient) stompClient.disconnect();
      if (eventSource) eventSource.close();
    });

    const resetOrderTrackingInfo = () => {
      longPollingOrderStatus.value = "PROCESSING";
      wsOrderStatus.value = "PROCESSING";
      sseOrderStatus.value = "PROCESSING";
      longPollingCanceledReason.value = null;
      wsCanceledReason.value = null;
      sseCanceledReason.value = null;
    };

    return {
      orderId,
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

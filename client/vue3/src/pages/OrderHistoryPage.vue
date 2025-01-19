<template>
  <div>
    <!-- Navbar Component -->
    <Navbar />

    <!-- Main Content -->
    <div class="container mt-4">
      <h3 class="my-4">Order History</h3>

      <ul class="list-group mb-4">
        <li v-for="orderPayload in orders" :key="orderPayload.id" class="list-group-item">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <p><strong>Order #{{ orderPayload.id }}</strong> - {{ formatDate(orderPayload.createdAt) }}</p>
              <p><strong>Amount:</strong> ${{ orderPayload.amount.toFixed(2) }}</p>
              <p>
                <strong class="me-1">Status:</strong>
                <span :class="applyBadgeClass(orderPayload.status)">{{ orderPayload.status }}</span>
              </p>
              <div v-if="orderPayload.status === 'CANCELED' && orderPayload.canceledReason" class="mt-3">
                <p><strong>Cancellation Reason:</strong> <span class="text-danger">{{ orderPayload.canceledReason }}</span></p>
              </div>
            </div>
            <button @click="toggleDetails(orderPayload)" class="btn btn-outline-secondary btn-sm">
              {{ isOrderSelected(orderPayload) ? "Hide Details" : "View Details" }}
            </button>
          </div>

          <!-- Order Details (visible when selected) -->
          <div v-if="isOrderSelected(orderPayload)" class="mt-3">
            <table class="table table-bordered mt-3">
              <thead>
                <tr>
                  <th>Product ID</th>
                  <th>Product Name</th>
                  <th>Price</th>
                  <th>Quantity</th>
                  <th>Subtotal</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in orderPayload.orderItems" :key="item.aggProduct.id">
                  <td>{{ item.aggProduct.id }}</td>
                  <td>{{ item.aggProduct.name }}</td>
                  <td>${{ item.aggProduct.price.toFixed(2) }}</td>
                  <td>{{ item.quantity }}</td>
                  <td>${{ (item.aggProduct.price * item.quantity).toFixed(2) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </li>
      </ul>

      <!-- Pagination Section -->
      <div v-if="totalPages > 1" class="d-flex justify-content-center align-items-center mt-4 gap-3">
        <button class="btn btn-outline-secondary" @click="changePage(currentPage - 1)" :disabled="currentPage === 1">
          Previous
        </button>
        <span>Page {{ currentPage }} of {{ totalPages }}</span>
        <button class="btn btn-outline-secondary" @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">
          Next
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import {onMounted, ref} from "vue";
import {listOrderApi} from "@/api/orderPayload";
import {applyBadgeClass} from "@/utils/orderStatusBadgeClass";
import Navbar from "@/components/Navbar.vue";

export default {
  components: {
    Navbar,
  },
  setup() {
    const orders = ref([]);
    const currentPage = ref(1);
    const pageSize = 20;
    const totalPages = ref(1);
    const selectedOrderId = ref(null);

    const fetchOrders = async () => {
      try {
        const params = { page: currentPage.value, size: pageSize };
        const { data } = await listOrderApi(params);
        orders.value = data.data.records;
        totalPages.value = data.data.totalPages;
      } catch (error) {
        console.error("Error fetching orders:", error);
      }
    };

    const changePage = (newPage) => {
      if (newPage > 0 && newPage <= totalPages.value) {
        currentPage.value = newPage;
        fetchOrders();
      }
    };

    const toggleDetails = (orderPayload) => {
      selectedOrderId.value = selectedOrderId.value === orderPayload.id ? null : orderPayload.id;
    };

    const isOrderSelected = (orderPayload) => selectedOrderId.value === orderPayload.id;
    const formatDate = (date) => new Date(date).toLocaleString();

    onMounted(fetchOrders);

    return {
      orders,
      currentPage,
      totalPages,
      changePage,
      toggleDetails,
      isOrderSelected,
      formatDate,
      applyBadgeClass,
    };
  },
};
</script>

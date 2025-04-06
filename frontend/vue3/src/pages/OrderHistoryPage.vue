<template>
  <div>
    <!-- Navbar Component -->
    <Navbar />

    <!-- Main Content -->
    <div class="container mt-4">
      <h3 class="my-4">Order History</h3>

      <ul class="list-group mb-4">
        <li v-for="order in orders" :key="order.id" class="list-group-item">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <p><strong>Order #{{ order.code }}</strong> - {{ order.createdAt }}</p>
              <p><strong>Amount:</strong> ${{ order.amount.toFixed(2) }}</p>
              <p>
                <strong class="me-1">Status:</strong>
                <span :class="applyBadgeClass(order.status)">{{ order.status }}</span>
              </p>
              <div v-if="order.status === 'CANCELED' && order.canceledReason" class="mt-3">
                <p><strong>Cancellation Reason:</strong> <span class="text-danger">{{ order.canceledReason }}</span></p>
              </div>
            </div>
            <button @click="toggleDetails(order)" class="btn btn-outline-secondary btn-sm">
              {{ isOrderSelected(order) ? "Hide Details" : "View Details" }}
            </button>
          </div>

          <!-- Order Details (visible when selected) -->
          <div v-if="isOrderSelected(order)" class="mt-3">
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
                <tr v-for="item in order.orderItems" :key="item.product.id">
                  <td>{{ item.product.id }}</td>
                  <td>{{ item.product.name }}</td>
                  <td>${{ item.product.price.toFixed(2) }}</td>
                  <td>{{ item.quantity }}</td>
                  <td>${{ (item.product.price * item.quantity).toFixed(2) }}</td>
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
import {api} from "@/api";
import {applyBadgeClass} from "@/utils/orderStatusBadgeClass";
import Navbar from "@/components/layout/Navbar.vue";

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
        const { data } = await api.orders.list(params);
        orders.value = data.data.results;
        totalPages.value = data.data.pagination.totalPages;
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

    const toggleDetails = (order) => {
      selectedOrderId.value = selectedOrderId.value === order.id ? null : order.id;
    };

    const isOrderSelected = (order) => selectedOrderId.value === order.id;

    onMounted(fetchOrders);

    return {
      orders,
      currentPage,
      totalPages,
      changePage,
      toggleDetails,
      isOrderSelected,
      applyBadgeClass,
    };
  },
};
</script>

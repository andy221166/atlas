<template>
  <div class="container-fluid p-2">
    <h2 class="h2 mb-4">Order Management</h2>

    <!-- Filters Card -->
    <div class="card mb-4">
      <div class="card-body">
        <form @submit.prevent="applyFilters(1)">
          <div class="row g-3">
            <div class="col-md-6">
              <label for="orderId" class="form-label">Order ID</label>
              <input
                id="orderId"
                v-model.trim="filters.orderId"
                type="text"
                placeholder="Enter order ID..."
                class="form-control"
              />
            </div>

            <div class="col-md-6">
              <label for="userId" class="form-label">User ID</label>
              <input
                id="userId"
                v-model.trim="filters.userId"
                type="text"
                placeholder="Enter user ID..."
                class="form-control"
              />
            </div>

            <div class="col-md-6">
              <label for="status" class="form-label">Status</label>
              <select id="status" v-model="filters.status" class="form-select">
                <option value="">All statuses</option>
                <option v-for="status in orderStatuses" :key="status" :value="status">
                  {{ formatOrderStatusLabel(status) }}
                </option>
              </select>
            </div>

            <div class="col-md-6">
              <label class="form-label">Date Range</label>
              <div class="input-group">
                <input
                  v-model="filters.startDate"
                  type="date"
                  placeholder="Start date"
                  class="form-control"
                />
                <span class="input-group-text">to</span>
                <input
                  v-model="filters.endDate"
                  type="date"
                  placeholder="End date"
                  class="form-control"
                />
              </div>
            </div>
          </div>

          <div class="d-flex gap-2 mt-4 pt-2 border-top">
            <button type="submit" class="btn btn-primary">
              <i class="bi bi-search me-1"></i> Search
            </button>
            <button type="button" @click="resetFilters" class="btn btn-outline-secondary">
              <i class="bi bi-arrow-counterclockwise me-1"></i> Reset
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Orders Table Card -->
    <div class="card">
      <div v-if="isLoadingOrders" class="text-center py-5" aria-live="polite">
        <div class="spinner-border" role="status" aria-label="Loading orders">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
      <div v-else class="card-body">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead class="table-light">
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Code</th>
                <th scope="col">User</th>
                <th scope="col">Amount</th>
                <th scope="col">Status</th>
                <th scope="col">Created At</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td>{{ order.id }}</td>
                <td>{{ order.code }}</td>
                <td>{{ order.user ? `${order.user.firstName} ${order.user.lastName}` : 'N/A' }}</td>
                <td>${{ formatCurrency(order.amount) }}</td>
                <td>
                  <span :class="getOrderStatusBadgeClasses(order.status)">
                    {{ formatOrderStatusLabel(order.status) }}
                  </span>
                </td>
                <td>{{ formatDate(order.createdAt) }}</td>
                <td>
                  <button
                    class="btn btn-sm btn-outline-secondary"
                    @click="toggleDetails(order.id)"
                  >
                    {{ selectedOrderId === order.id ? 'Hide Details' : 'View Details' }}
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Order Details -->
        <template v-for="order in orders" :key="'details-' + order.id">
          <div v-show="selectedOrderId === order.id" class="mt-3">
            <h6 class="mb-3">User Information</h6>
            <dl class="row mb-3">
              <dt class="col-sm-3">User ID</dt>
              <dd class="col-sm-9">{{ order.user?.id ?? 'N/A' }}</dd>
              <dt class="col-sm-3">First Name</dt>
              <dd class="col-sm-9">{{ order.user?.firstName ?? 'N/A' }}</dd>
              <dt class="col-sm-3">Last Name</dt>
              <dd class="col-sm-9">{{ order.user?.lastName ?? 'N/A' }}</dd>
            </dl>

            <div v-if="order.cancelReason" class="mb-3">
              <strong>Cancellation Reason:</strong>
              <span class="text-danger">{{ order.cancelReason }}</span>
            </div>

            <h6 class="mb-3">Order Items</h6>
            <table class="table table-bordered">
              <thead>
                <tr>
                  <th scope="col">Product ID</th>
                  <th scope="col">Product Name</th>
                  <th scope="col">Price</th>
                  <th scope="col">Quantity</th>
                  <th scope="col">Subtotal</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in order.orderItems" :key="item.product.id">
                  <td>{{ item.product.id }}</td>
                  <td>{{ item.product.name }}</td>
                  <td>${{ formatCurrency(item.product.price) }}</td>
                  <td>{{ item.quantity }}</td>
                  <td>${{ formatCurrency(item.product.price * item.quantity) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </template>

        <!-- Pagination -->
        <div class="card-footer bg-light">
          <div class="d-flex justify-content-between align-items-center">
            <span class="text-muted">
              Page {{ metadata.currentPage }} of {{ metadata.totalPages }}
              ({{ metadata.totalRecords }} records)
            </span>
            <div class="btn-group">
              <button
                @click="changePage(metadata.currentPage - 1)"
                :disabled="metadata.currentPage <= 1"
                class="btn btn-outline-secondary"
              >
                Previous
              </button>
              <button
                @click="changePage(metadata.currentPage + 1)"
                :disabled="metadata.currentPage >= metadata.totalPages"
                class="btn btn-outline-secondary"
              >
                Next
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { formatCurrency, formatDate, formatOrderStatusLabel, getOrderStatusBadgeClasses } from '@/utils/formatter.util';
import { computed, onMounted, reactive, ref } from 'vue';
import { toast } from 'vue3-toastify';
import { listOrder } from '../../services/order.admin.service';
import { OrderStatus, type ListOrderFilters, type Order } from '../../types/order.interface';

const orders = ref<Order[]>([]);
const isLoadingOrders = ref(true);
const selectedOrderId = ref<number | null>(null);
const filters = reactive<ListOrderFilters>({
  orderId: undefined,
  userId: undefined,
  status: '',
  startDate: undefined,
  endDate: undefined,
  page: 1,
  size: 20,
});
const metadata = reactive({
  currentPage: 1,
  pageSize: 20,
  totalPages: 1,
  totalRecords: 0,
});

// Computed property for order statuses
const orderStatuses = computed(() => Object.values(OrderStatus));

const toggleDetails = (orderId: number) => {
  selectedOrderId.value = selectedOrderId.value === orderId ? null : orderId;
};

const applyFilters = async (page: number) => {
  if (isLoadingOrders.value) return;

  isLoadingOrders.value = true;
  try {
    filters.page = page; // Set the page in filters
    metadata.currentPage = page; // Sync metadata with the provided page
    const response = await listOrder(filters);
    orders.value = response.data;
    Object.assign(metadata, response.metadata);
    selectedOrderId.value = null;
  } catch (error) {
    toast.error('Failed to load orders');
  } finally {
    isLoadingOrders.value = false;
  }
};

const changePage = (newPage: number) => {
  if (newPage >= 1 && newPage <= metadata.totalPages) {
    applyFilters(newPage);
  }
};

const resetFilters = () => {
  Object.assign(filters, {
    id: undefined,
    userId: undefined,
    status: '',
    startDate: undefined,
    endDate: undefined,
    page: 1,
    size: 20,
  });
  applyFilters(1); // Reset to page 1
};

// Initial load
onMounted(() => applyFilters(1));
</script>

<style scoped>
.table-responsive {
  min-height: 200px;
}
</style>

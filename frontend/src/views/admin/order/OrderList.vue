<template>
  <div class="container-fluid p-2">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="h2">Order Management</h2>
    </div>

    <!-- Filters -->
    <div class="card mb-4">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Order ID</label>
            <input v-model="filters.id" type="text" placeholder="Enter order ID..." class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">User ID</label>
            <input v-model="filters.userId" type="text" placeholder="Enter user ID..." class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">Status</label>
            <select v-model="filters.status" class="form-select">
              <option value="">All statuses</option>
              <option v-for="status in Object.values(OrderStatus)" :key="status" :value="status">
                {{ formatStatusLabel(status) }}
              </option>
            </select>
          </div>

          <div class="col-md-6">
            <label class="form-label">Date Range</label>
            <div class="input-group">
              <input v-model="filters.startDate" type="date" placeholder="Start date" class="form-control" />
              <span class="input-group-text">to</span>
              <input v-model="filters.endDate" type="date" placeholder="End date" class="form-control" />
            </div>
          </div>
        </div>

        <div class="d-flex justify-content-start mt-4 pt-2 border-top">
          <button @click="applyFilters" class="btn btn-primary me-2">
            <i class="bi bi-search me-1"></i> Search
          </button>
          <button @click="resetFilters" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-counterclockwise me-1"></i> Reset
          </button>
        </div>
      </div>
    </div>

    <!-- Orders Table -->
    <div class="card">
      <div v-if="isLoadingOrders" class="text-center py-5">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading orders...</span>
        </div>
      </div>
      <div v-else class="card-body">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead class="table-light">
              <tr>
                <th>ID</th>
                <th>Code</th>
                <th>User</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Created At</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td>{{ order.id }}</td>
                <td>{{ order.code }}</td>
                <td>{{ order.user ? `${order.user.firstName} ${order.user.lastName}` : 'N/A' }}</td>
                <td>${{ order.amount.toFixed(2) }}</td>
                <td>
                  <span :class="{
                    'badge': true,
                    'bg-success': order.status === 'COMPLETED',
                    'bg-warning': order.status === 'PENDING',
                    'bg-danger': order.status === 'CANCELLED',
                    'bg-secondary': order.status === 'PROCESSING'
                  }">
                    {{ formatStatusLabel(order.status) }}
                  </span>
                </td>
                <td>{{ formatDate(order.createdAt) }}</td>
                <td>
                  <button class="btn btn-sm btn-outline-secondary"
                    @click="toggleDetails(order)">
                    {{ isOrderSelected(order) ? 'Hide Details' : 'View Details' }}
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Order Details -->
        <div v-for="order in orders" :key="'details-' + order.id" v-show="isOrderSelected(order)" class="mt-3">
          <h6 class="mb-3">User Information</h6>
          <dl class="row mb-3">
            <dt class="col-sm-3">User ID</dt>
            <dd class="col-sm-9">{{ order.user ? order.user.id : 'N/A' }}</dd>
            <dt class="col-sm-3">First Name</dt>
            <dd class="col-sm-9">{{ order.user ? order.user.firstName : 'N/A' }}</dd>
            <dt class="col-sm-3">Last Name</dt>
            <dd class="col-sm-9">{{ order.user ? order.user.lastName : 'N/A' }}</dd>
          </dl>

          <div v-if="order.cancelReason" class="mb-3">
            <strong>Cancellation Reason:</strong> <span class="text-danger">{{ order.cancelReason }}</span>
          </div>

          <h6 class="mb-3">Order Items</h6>
          <table class="table table-bordered">
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

        <!-- Pagination -->
        <div class="card-footer bg-light">
          <div class="d-flex justify-content-between align-items-center">
            <span class="text-muted">
              Showing page {{ metadata.currentPage }} of {{ metadata.totalPages }}
              (Total: {{ metadata.totalRecords }} records)
            </span>
            <div class="btn-group">
              <button @click="changePage(metadata.currentPage - 1)" :disabled="metadata.currentPage <= 1"
                class="btn btn-outline-secondary">
                Previous
              </button>
              <button @click="changePage(metadata.currentPage + 1)"
                :disabled="metadata.currentPage >= metadata.totalPages" class="btn btn-outline-secondary">
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
import { ref, reactive, onMounted } from 'vue';
import { toast } from 'vue3-toastify';
import { listOrder, OrderStatus, type ListOrderFilters, type Order } from '@/services/order/order.admin.service';

const orders = ref<Order[]>([]);
const isLoadingOrders = ref(true);
const selectedOrderId = ref<number | null>(null);
  const filters = reactive<ListOrderFilters>({
  id: undefined,
  userId: undefined,
  status: '',
  startDate: undefined,
  endDate: undefined,
  page: 1,
  size: 20
});
const metadata = reactive({
  currentPage: 1,
  pageSize: 20,
  totalPages: 1,
  totalRecords: 0
});

const formatStatusLabel = (status: OrderStatus): string => {
  return status
    .split('_')
    .map(word => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

const formatDate = (date: string): string => {
  return new Date(date).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

const toggleDetails = (order: Order) => {
  selectedOrderId.value = selectedOrderId.value === order.id ? null : order.id;
};

const isOrderSelected = (order: Order): boolean => {
  return selectedOrderId.value === order.id;
};

const applyFilters = async () => {
  isLoadingOrders.value = true;
  try {
    filters.page = metadata.currentPage;
    const response = await listOrder(filters);
    orders.value = response.data;
    Object.assign(metadata, response.metadata);
    // Reset selected order when filters are applied
    selectedOrderId.value = null;
  } catch (error) {
    toast.error('Failed to load orders. Please try again.');
  } finally {
    isLoadingOrders.value = false;
  }
};

const changePage = (newPage: number) => {
  if (newPage >= 1 && newPage <= metadata.totalPages) {
    metadata.currentPage = newPage;
    applyFilters();
  }
};

const resetFilters = () => {
  Object.assign(filters, {
    id: undefined,
    userId: undefined,
    status: undefined,
    startDate: undefined,
    endDate: undefined,
    page: 1,
    size: 20
  });
  metadata.currentPage = 1;
  applyFilters();
};

onMounted(async () => {
  await applyFilters();
});
</script>

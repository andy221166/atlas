<template>
  <div class="container-fluid p-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1 class="h2">Product Management</h1>
      <button class="btn btn-primary">
        Add New Product
      </button>
    </div>

    <!-- Filters -->
    <div class="card mb-4">
      <div class="card-body">
        <div class="row g-3">
          <!-- Search and Price Range - First Row -->
          <div class="col-md-6">
            <label class="form-label">Search</label>
            <input v-model="filters.keyword" type="text" placeholder="Search by product name..." class="form-control" />
          </div>
          <div class="col-md-6">
            <label class="form-label">Price Range</label>
            <div class="input-group">
              <input v-model="filters.minPrice" type="number" placeholder="Min" class="form-control" />
              <span class="input-group-text">to</span>
              <input v-model="filters.maxPrice" type="number" placeholder="Max" class="form-control" />
            </div>
          </div>

          <!-- Status and Date - Second Row -->
          <div class="col-md-6">
            <label class="form-label">Status</label>
            <select v-model="filters.status" class="form-select">
              <option value="">All status</option>
              <option v-for="status in Object.values(ProductStatus)" :key="status" :value="status">
                {{ formatStatusLabel(status) }}
              </option>
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Available From</label>
            <input v-model="filters.availableFrom" type="date" class="form-control" />
          </div>

          <!-- Active Status - Third Row -->
          <div class="col-12">
            <label class="form-label">Activity</label>
            <div class="border rounded p-2">
              <div class="d-flex gap-4">
                <div class="form-check">
                  <input type="checkbox" class="form-check-input" id="activeCheck" v-model="activeStates.active"
                    @change="handleActiveStateChange" />
                  <label class="form-check-label" for="activeCheck">Active</label>
                </div>
                <div class="form-check">
                  <input type="checkbox" class="form-check-input" id="inactiveCheck" v-model="activeStates.inactive"
                    @change="handleActiveStateChange" />
                  <label class="form-check-label" for="inactiveCheck">Inactive</label>
                </div>
              </div>
            </div>
          </div>

          <!-- Brand and Categories - Fourth Row -->
          <div class="col-md-6">
            <label class="form-label">Brand</label>
            <select v-model="filters.brandId" class="form-select" :disabled="!brands.length">
              <option value="" selected>All brands</option>
              <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                {{ brand.name }}
              </option>
            </select>
            <div v-if="!brands.length" class="text-muted small mt-1">
              Loading brands...
            </div>
          </div>
          <div class="col-md-6">
            <label class="form-label">Categories</label>
            <select v-model="filters.categoryIds" multiple class="form-select" :disabled="!categories.length" size="3">
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
            <div v-if="!categories.length" class="text-muted small mt-1">
              Loading categories...
            </div>
            <small class="text-muted">Hold Ctrl/Cmd to select multiple categories</small>
          </div>
        </div>

        <!-- Action Buttons -->
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

    <!-- Products Table -->
    <div class="card">
      <div class="table-responsive">
        <table class="table table-hover">
          <thead class="table-light">
            <tr>
              <th>Image</th>
              <th>Name</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Status</th>
              <th>Available From</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in products" :key="product.id">
              <td>
                <img :src="product.imageUrl" :alt="product.name" class="img-thumbnail"
                  style="width: 48px; height: 48px; object-fit: cover;" />
              </td>
              <td>{{ product.name }}</td>
              <td>${{ product.price.toFixed(2) }}</td>
              <td>{{ product.quantity }}</td>
              <td>
                <span :class="{
                  'badge': true,
                  'bg-success': product.status === 'IN_STOCK',
                  'bg-danger': product.status === 'OUT_STOCK',
                  'bg-secondary': product.status === 'DISCONTINUED'
                }">
                  {{ product.status }}
                </span>
              </td>
              <td>{{ formatDate(product.availableFrom) }}</td>
              <td>
                <button class="btn btn-sm btn-outline-primary me-2">Edit</button>
                <button class="btn btn-sm btn-outline-danger">Delete</button>
              </td>
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
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { listProduct, ProductStatus, type ListProductFilters, type Product } from '@/services/product/product.service';
import { formatDate } from '@/utils/date.util';
import { listBrand, type Brand } from '@/services/product/brand.service';
import { listCategory, type Category } from '@/services/product/category.service';

const activeStates = reactive({
  active: true,
  inactive: true
});
const brands = ref<Brand[]>([]);
const categories = ref<Category[]>([]);
const products = ref<Product[]>([]);
const metadata = reactive({
  currentPage: 1,
  pageSize: 20,
  totalPages: 1,
  totalRecords: 0
});

const filters = reactive<ListProductFilters>({
  keyword: '',
  minPrice: null,
  maxPrice: null,
  status: '',
  availableFrom: undefined,
  isActive: null,
  brandId: '',
  categoryIds: [],
  page: 1,
  size: 20
});

const formatStatusLabel = (status: ProductStatus): string => {
  return status.split('_')
    .map(word => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

const loadBrandsAndCategories = async () => {
  const [brandsResponse, categoriesResponse] = await Promise.all([
    listBrand(),
    listCategory()
  ]);

  if (brandsResponse.success) {
    brands.value = brandsResponse.data;
  }
  if (categoriesResponse.success) {
    categories.value = categoriesResponse.data;
  }
};

const handleActiveStateChange = () => {
  if (activeStates.active && activeStates.inactive) {
    // Both checked - don't set any value
    filters.isActive = null;
  } else if (activeStates.active) {
    // Only active checked
    filters.isActive = true;
  } else if (activeStates.inactive) {
    // Only inactive checked
    filters.isActive = false;
  } else {
    // Neither checked - don't set any value
    filters.isActive = null;
  }
};

const applyFilters = async (event?: MouseEvent) => {
  filters.page = metadata.currentPage;
  const response = await listProduct(filters);

  if (response.success) {
    products.value = response.data;
    Object.assign(metadata, response.metadata);
  }
};

const changePage = (newPage: number) => {
  if (newPage >= 1 && newPage <= metadata.totalPages) {
    metadata.currentPage = newPage;
    applyFilters();
  }
};

const resetFilters = () => {
  // Reset checkboxes to their default state (both checked)
  activeStates.active = true;
  activeStates.inactive = true;

  Object.assign(filters, {
    keyword: '',
    minPrice: null,
    maxPrice: null,
    status: '',
    isActive: null,
    brandId: '',
    categoryIds: [],
    page: 1,
    size: 20
  });
  metadata.currentPage = 1;
  applyFilters();
};

onMounted(async () => {
  await loadBrandsAndCategories();
  applyFilters();
});
</script>

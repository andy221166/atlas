<template>
  <div class="container-fluid p-2">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="h2">Product Management</h2>
      <button class="btn btn-success" @click="router.push({ name: 'productAdd' })">
        <i class="bi bi-plus-lg me-1"></i> Add New Product
      </button>
    </div>

    <!-- Filters Card -->
    <div class="card mb-4">
      <div class="card-body">
        <form @submit.prevent="applyFilters(1)">
          <div class="row g-3">
            <div class="col-md-6">
              <label for="productId" class="form-label">Product ID</label>
              <input
                id="productId"
                v-model.trim="filters.id"
                type="text"
                placeholder="Enter product ID..."
                class="form-control"
              />
            </div>

            <div class="col-md-6">
              <label for="keyword" class="form-label">Search</label>
              <input
                id="keyword"
                v-model.trim="filters.keyword"
                type="text"
                placeholder="Search by product name or description..."
                class="form-control"
              />
            </div>

            <div class="col-md-6">
              <label class="form-label">Price Range</label>
              <div class="input-group">
                <input
                  v-model.number="filters.minPrice"
                  type="number"
                  placeholder="Min"
                  class="form-control"
                />
                <span class="input-group-text">to</span>
                <input
                  v-model.number="filters.maxPrice"
                  type="number"
                  placeholder="Max"
                  class="form-control"
                />
              </div>
            </div>

            <div class="col-md-6">
              <label for="status" class="form-label">Status</label>
              <select id="status" v-model="filters.status" class="form-select">
                <option value="">All statuses</option>
                <option v-for="status in productStatuses" :key="status" :value="status">
                  {{ formatProductStatusLabel(status) }}
                </option>
              </select>
            </div>

            <div class="col-md-6">
              <label for="availableFrom" class="form-label">Available From</label>
              <input
                id="availableFrom"
                v-model="filters.availableFrom"
                type="date"
                class="form-control"
              />
            </div>

            <div class="col-md-6">
              <label class="form-label">Activity</label>
              <div class="border rounded p-2">
                <div class="d-flex gap-4">
                  <div class="form-check">
                    <input
                      type="checkbox"
                      class="form-check-input"
                      id="activeCheck"
                      v-model="activeStates.active"
                      @change="handleActiveStateChange"
                    />
                    <label class="form-check-label" for="activeCheck">Active</label>
                  </div>
                  <div class="form-check">
                    <input
                      type="checkbox"
                      class="form-check-input"
                      id="inactiveCheck"
                      v-model="activeStates.inactive"
                      @change="handleActiveStateChange"
                    />
                    <label class="form-check-label" for="inactiveCheck">Inactive</label>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-6">
              <label for="brandId" class="form-label">Brand</label>
              <div v-if="isLoadingBrands" class="text-center py-2" aria-live="polite">
                <div class="spinner-border spinner-border-sm" role="status" aria-label="Loading brands">
                  <span class="visually-hidden">Loading...</span>
                </div>
              </div>
              <select
                v-else
                id="brandId"
                v-model="filters.brandId"
                class="form-select"
                :disabled="!brands.length"
              >
                <option value="">All brands</option>
                <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                  {{ brand.name }}
                </option>
              </select>
              <div v-if="!brands.length && !isLoadingBrands" class="text-muted small mt-1">
                No brands available
              </div>
            </div>

            <div class="col-md-6">
              <label for="categoryIds" class="form-label">Categories</label>
              <div v-if="isLoadingCategories" class="text-center py-2" aria-live="polite">
                <div class="spinner-border spinner-border-sm" role="status" aria-label="Loading categories">
                  <span class="visually-hidden">Loading...</span>
                </div>
              </div>
              <select
                v-else
                id="categoryIds"
                v-model="filters.categoryIds"
                multiple
                class="form-select"
                :disabled="!categories.length"
                size="3"
              >
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.name }}
                </option>
              </select>
              <div v-if="!categories.length && !isLoadingCategories" class="text-muted small mt-1">
                No categories available
              </div>
              <small class="text-muted">Hold Ctrl/Cmd to select multiple</small>
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

    <!-- Products Table -->
    <div class="card">
      <div v-if="isLoadingProducts" class="text-center py-5" aria-live="polite">
        <div class="spinner-border" role="status" aria-label="Loading products">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
      <div v-else class="card-body">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead class="table-light">
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Name</th>
                <th scope="col">Image</th>
                <th scope="col">Price</th>
                <th scope="col">Quantity</th>
                <th scope="col">Status</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in products" :key="product.id">
                <td>{{ product.id }}</td>
                <td>{{ product.name }}</td>
                <td>
                  <img
                    :src="getProductImageUrl(product.image)"
                    :alt="product.name"
                    class="img-thumbnail"
                    style="width: 48px; height: 48px; object-fit: cover;"
                  />
                </td>
                <td>${{ formatCurrency(product.price) }}</td>
                <td>{{ product.quantity }}</td>
                <td>
                  <span :class="getStatusBadgeClasses(product.status)">
                    {{ formatProductStatusLabel(product.status) }}
                  </span>
                </td>
                <td>
                  <button
                    class="btn btn-sm btn-outline-secondary me-1"
                    @click="router.push({ name: 'productInfo', params: { id: product.id } })"
                  >
                    View
                  </button>
                  <button
                    class="btn btn-sm btn-outline-primary me-1"
                    @click="router.push({ name: 'productEdit', params: { id: product.id } })"
                  >
                    <i class="bi bi-pencil me-1"></i> Edit
                  </button>
                  <button
                    class="btn btn-sm btn-outline-danger"
                    @click="handleDelete(product.id)"
                  >
                    <i class="bi bi-trash me-1"></i> Delete
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

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
import { getProductImageUrl } from '@/features/product/utils/product.util';
import { useFlashStore } from '@/stores/flash.store';
import { formatCurrency, formatProductStatusLabel } from '@/utils/formatter.util';
import { computed, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { toast } from 'vue3-toastify';
import { deleteProduct, listProduct } from '../../services/product.admin.service';
import { listBrand, listCategory } from '../../services/product.common.service';
import { ProductStatus } from '../../types/product.enum';
import type { Brand, Category, ListProductFilters, Product } from '../../types/product.interface';

const router = useRouter();
const flashStore = useFlashStore();

const activeStates = reactive({
  active: true,
  inactive: true,
});
const brands = ref<Brand[]>([]);
const categories = ref<Category[]>([]);
const products = ref<Product[]>([]);
const isLoadingBrands = ref(true);
const isLoadingCategories = ref(true);
const isLoadingProducts = ref(true);
const metadata = reactive({
  currentPage: 1,
  pageSize: 20,
  totalPages: 1,
  totalRecords: 0,
});
const filters = reactive<ListProductFilters>({
  id: undefined,
  keyword: undefined,
  minPrice: undefined,
  maxPrice: undefined,
  status: '',
  availableFrom: undefined,
  isActive: undefined,
  brandId: undefined,
  categoryIds: [],
  page: 1,
  size: 20,
});

// Computed property for product statuses
const productStatuses = computed(() => Object.values(ProductStatus));

// Computed property for status badge classes
const getStatusBadgeClasses = (status: string) =>
  computed(() => ({
    badge: true,
    'bg-success': status === ProductStatus.IN_STOCK,
    'bg-danger': status === ProductStatus.OUT_STOCK,
    'bg-secondary': status === ProductStatus.DISCONTINUED,
  }));

const loadBrands = async () => {
  isLoadingBrands.value = true;
  try {
    const { data } = await listBrand();
    brands.value = data;
  } catch (error) {
    toast.error('Failed to load brands');
  } finally {
    isLoadingBrands.value = false;
  }
};

const loadCategories = async () => {
  isLoadingCategories.value = true;
  try {
    const { data } = await listCategory();
    categories.value = data;
  } catch (error) {
    toast.error('Failed to load categories');
  } finally {
    isLoadingCategories.value = false;
  }
};

const handleActiveStateChange = () => {
  if (activeStates.active && activeStates.inactive) {
    filters.isActive = undefined;
  } else if (activeStates.active) {
    filters.isActive = true;
  } else if (activeStates.inactive) {
    filters.isActive = false;
  } else {
    filters.isActive = undefined;
  }
};

const applyFilters = async (page: number) => {
  if (isLoadingProducts.value) return;

  isLoadingProducts.value = true;
  try {
    filters.page = page;
    metadata.currentPage = page;
    const response = await listProduct(filters);
    products.value = response.data;
    Object.assign(metadata, response.metadata);
  } catch (error) {
    toast.error('Failed to load products');
  } finally {
    isLoadingProducts.value = false;
  }
};

const changePage = (newPage: number) => {
  if (newPage >= 1 && newPage <= metadata.totalPages) {
    applyFilters(newPage);
  }
};

const resetFilters = () => {
  activeStates.active = true;
  activeStates.inactive = true;
  Object.assign(filters, {
    id: undefined,
    keyword: undefined,
    minPrice: undefined,
    maxPrice: undefined,
    status: '',
    availableFrom: undefined,
    isActive: undefined,
    brandId: undefined,
    categoryIds: [],
    page: 1,
    size: 20,
  });
  applyFilters(1);
};

const handleDelete = async (productId: number) => {
  if (!confirm('Are you sure you want to delete this product?')) return;

  try {
    await deleteProduct(productId);
    toast.success('Product deleted successfully');
    applyFilters(metadata.currentPage); // Preserve current page after deletion
  } catch (error) {
    toast.error('Failed to delete product');
  }
};

onMounted(async () => {
  await Promise.all([
    loadBrands(),
    loadCategories(),
    applyFilters(1),
  ]);
  if (flashStore.successMessage) toast.success(flashStore.successMessage);
  if (flashStore.errorMessage) toast.error(flashStore.errorMessage);
});
</script>

<style scoped>
.table-responsive {
  min-height: 200px;
}
</style>

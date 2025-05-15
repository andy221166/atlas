<template>
  <div class="container-fluid p-2">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="h2">Product Management</h2>
      <button class="btn btn-success" @click="router.push({ name: 'productAdd' })">
        <i class="bi bi-plus-lg me-1"></i> Add New Product
      </button>
    </div>

    <!-- Filters -->
    <div class="card mb-4">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Product ID</label>
            <input v-model="filters.id" type="text" placeholder="Enter product ID..." class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">Search</label>
            <input v-model="filters.keyword" type="text" placeholder="Search by product name or description..."
              class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">Price Range</label>
            <div class="input-group">
              <input v-model="filters.minPrice" type="number" placeholder="Min" class="form-control" />
              <span class="input-group-text">to</span>
              <input v-model="filters.maxPrice" type="number" placeholder="Max" class="form-control" />
            </div>
          </div>

          <div class="col-md-6">
            <label class="form-label">Status</label>
            <select v-model="filters.status" class="form-select">
              <option value="">All status</option>
              <option v-for="status in Object.values(ProductStatus)" :key="status" :value="status">
                {{ formatProductStatusLabel(status) }}
              </option>
            </select>
          </div>

          <div class="col-md-6">
            <label class="form-label">Available From</label>
            <input v-model="filters.availableFrom" type="date" class="form-control" />
          </div>

          <div class="col-md-6">
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

          <div class="col-md-6">
            <label class="form-label">Brand</label>
            <div v-if="isLoadingBrands" class="text-center py-2">
              <div class="spinner-border spinner-border-sm" role="status">
                <span class="visually-hidden">Loading brands...</span>
              </div>
            </div>
            <select v-else v-model="filters.brandId" class="form-select" :disabled="!brands.length">
              <option value="" selected>All brands</option>
              <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                {{ brand.name }}
              </option>
            </select>
            <div v-if="!brands.length && !isLoadingBrands" class="text-muted small mt-1">
              No brands available
            </div>
          </div>

          <div class="col-md-6">
            <label class="form-label">Categories</label>
            <div v-if="isLoadingCategories" class="text-center py-2">
              <div class="spinner-border spinner-border-sm" role="status">
                <span class="visually-hidden">Loading categories...</span>
              </div>
            </div>
            <select v-else v-model="filters.categoryIds" multiple class="form-select" :disabled="!categories.length"
              size="3">
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
            <div v-if="!categories.length && !isLoadingCategories" class="text-muted small mt-1">
              No categories available
            </div>
            <small class="text-muted">Hold Ctrl/Cmd to select multiple categories</small>
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

    <!-- Products Table -->
    <div class="card">
      <div v-if="isLoadingProducts" class="text-center py-5">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading products...</span>
        </div>
      </div>
      <div v-else class="card-body">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead class="table-light">
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Image</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in products" :key="product.id">
                <td>{{ product.id }}</td>
                <td>{{ product.name }}</td>
                <td>
                  <img :src="getProductImageUrl(product.image)" :alt="product.name" class="img-thumbnail"
                    style="width: 48px; height: 48px; object-fit: cover;" />
                </td>
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
                <td>
                  <button class="btn btn-sm btn-outline-secondary me-2"
                    @click="router.push({ name: 'productInfo', params: { id: product.id } })">
                    View
                  </button>
                  <button class="btn btn-sm btn-outline-primary me-2"
                    @click="router.push({ name: 'productEdit', params: { id: product.id } })">
                    <i class="bi bi-pencil me-1"></i> Edit
                  </button>
                  <button class="btn btn-sm btn-outline-danger" @click="handleDelete(product.id)">
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
import { useFlashStore } from '@/stores/flash.store';
import { formatProductStatusLabel, getProductImage } from '@/features/product/utils/product.util';
import { onMounted, reactive, ref } from 'vue';
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
  inactive: true
});
const brands = ref<Brand[]>([]);
const categories = ref<Category[]>([]);
const products = ref<Product[]>([]);
const isLoadingBrands = ref<boolean>(true);
const isLoadingCategories = ref<boolean>(true);
const isLoadingProducts = ref<boolean>(true);
const metadata = reactive({
  currentPage: 1,
  pageSize: 20,
  totalPages: 1,
  totalRecords: 0
});
const filters = reactive<ListProductFilters>({
  id: undefined,
  keyword: undefined,
  minPrice: undefined,
  maxPrice: undefined,
  status: undefined,
  availableFrom: undefined,
  isActive: undefined,
  brandId: undefined,
  categoryIds: [],
  page: metadata.currentPage,
  size: metadata.pageSize
});

const loadBrands = async () => {
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
  isLoadingProducts.value = true;
  try {
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
    metadata.currentPage = newPage;
    applyFilters();
  }
};

const resetFilters = () => {
  // Reset checkboxes to their default state (both checked)
  activeStates.active = true;
  activeStates.inactive = true;

  Object.assign(filters, {
    id: '',
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

const handleDelete = async (productId: number) => {
  if (!confirm('Are you sure you want to delete this product?')) {
    return;
  }

  try {
    await deleteProduct(productId);
    toast.success('Deleted product successfully');

    // Refresh the product list
    applyFilters();
  } catch (error) {
    toast.error('Failed to delete the product');
  }
};

onMounted(async () => {
  await Promise.all([
    loadBrands(),
    loadCategories(),
    applyFilters(),
  ]);

  if (flashStore.successMessage) {
    toast.success(flashStore.successMessage);
  }

  if (flashStore.errorMessage) {
    toast.error(flashStore.errorMessage);
  }
});
</script>

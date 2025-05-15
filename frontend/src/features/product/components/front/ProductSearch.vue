<template>
  <div class="search-product px-3 mb-3">
    <!-- Merged ProductSearchForm -->
    <form @submit.prevent="applyFilters" class="bg-light p-4 rounded-3 mb-4">
      <div class="row g-3">
        <!-- Search Input -->
        <div class="col-md-12">
          <div class="form-group">
            <input v-model="filters.keyword" type="text" class="form-control" placeholder="Search products..." />
          </div>
        </div>

        <!-- Price Range -->
        <div class="col-md-12">
          <div class="row g-3">
            <div class="col-md-6">
              <div class="form-group">
                <input v-model="filters.minPrice" type="number" class="form-control" placeholder="Min Price" min="0"
                  step="0.01" />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <input v-model="filters.maxPrice" type="number" class="form-control" placeholder="Max Price" min="0"
                  step="0.01" />
              </div>
            </div>
          </div>
        </div>

        <!-- Brand and Category Filters -->
        <div class="col-md-12">
          <div class="row g-3">
            <div class="col-md-6">
              <div class="form-group">
                <select v-model="filters.brandId" class="form-select">
                  <option value="all">All Brands</option>
                  <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                    {{ brand.name }}
                  </option>
                </select>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <select v-model="filters.categoryIds" class="form-select" multiple>
                  <option value="all">All Categories</option>
                  <option v-for="category in categories" :key="category.id" :value="category.id">
                    {{ category.name }}
                  </option>
                </select>
              </div>
            </div>
          </div>
        </div>

        <!-- Submit Button -->
        <div class="col-md-12 mt-3">
          <button type="submit" class="btn btn-primary">
            <i class="bi bi-search me-2"></i>Search
          </button>
        </div>
      </div>
    </form>

    <div v-if="isLoadingProducts" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading products...</span>
      </div>
    </div>
    <div v-if="products.length" class="mt-4 row g-3">
      <div v-for="product in products" :key="product.id" class="col-md-4">
        <div class="card h-100 shadow-sm">
          <img :src="getProductImageUrl(product.image)" class="card-img-top product-image" :alt="product.name" />
          <div class="card-body d-flex flex-column">
            <h5 class="card-title product-name" @click="showProductDetails(product)">
              {{ product.name }}
            </h5>
            <h6 class="card-subtitle mb-3 text-muted">
              ${{ product.price.toFixed(2) }}
            </h6>
            <button @click="handleAddToCart(product)" class="btn btn-primary mt-auto">
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </div>
    <p v-else class="text-center text-muted mt-3">No products found.</p>

    <ProductDetailsModal v-model="showModal" :product="selectedProduct" :is-loading="isLoadingProduct" />

    <div v-if="metadata.totalPages > 1" class="d-flex justify-content-center align-items-center mt-4 gap-3">
      <button class="btn btn-secondary" @click="changePage(metadata.currentPage - 1)"
        :disabled="metadata.currentPage === 1">
        Previous
      </button>
      <span>Page {{ metadata.currentPage }} of {{ metadata.totalPages }}</span>
      <button class="btn btn-secondary" @click="changePage(metadata.currentPage + 1)"
        :disabled="metadata.currentPage === metadata.totalPages">
        Next
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import { toast } from "vue3-toastify";
import { useCartStore } from "@/stores/cart.store";
import type { Brand, Category, Product, SearchProductFilters } from "../../types/product.interface";
import { listBrand, listCategory } from "../../services/product.common.service";
import { searchProduct } from "../../services/product.front.service";
import { getProductImage } from "../../utils/product.util";

const cartStore = useCartStore();

const showModal = ref(false);
const products = ref<Product[]>([]);
const categories = ref<Category[]>([]);
const brands = ref<Brand[]>([]);
const selectedProduct = ref<Product | null>(null);
const isLoadingProducts = ref(false);
const isLoadingProduct = ref(false);

const metadata = reactive({
  currentPage: 1,
  pageSize: 9,
  totalPages: 1,
  totalRecords: 0
});

// Filters for API
const filters = reactive<SearchProductFilters>({
  keyword: undefined,
  minPrice: undefined,
  maxPrice: undefined,
  brandId: undefined,
  categoryIds: undefined,
  page: metadata.currentPage,
  size: metadata.pageSize
});

const loadBrands = async () => {
  try {
    const { data } = await listBrand();
    brands.value = data;
  } catch {
    toast.error('Failed to load brands');
  }
};

const loadCategories = async () => {
  try {
    const { data } = await listCategory();
    categories.value = data;
  } catch {
    toast.error('Failed to load categories');
  }
};

const loadProducts = async () => {
  isLoadingProducts.value = true;
  try {
    filters.page = metadata.currentPage;
    filters.size = metadata.pageSize;
    const response = await searchProduct(filters);
    products.value = response.data;
    Object.assign(metadata, {
      currentPage: response.metadata?.currentPage ?? 1,
      pageSize: response.metadata?.pageSize ?? 9,
      totalPages: response.metadata?.totalPages ?? 1,
      totalRecords: response.metadata?.totalRecords ?? 0
    });
  } catch (error) {
    toast.error('Error fetching products');
  } finally {
    isLoadingProducts.value = false;
  }
};

const changePage = async (newPage: number) => {
  if (newPage < 1 || newPage > metadata.totalPages) return;
  metadata.currentPage = newPage;
  await loadProducts();
};

const showProductDetails = (product: Product) => {
  selectedProduct.value = product;
  showModal.value = true;
};

const applyFilters = async (event?: Event) => {
  event?.preventDefault();
  isLoadingProducts.value = true;
  try {
    filters.page = metadata.currentPage;
    const response = await searchProduct(filters);
    products.value = response.data;
    Object.assign(metadata, response.metadata);
  } catch (error) {
    toast.error('Failed to search products');
  } finally {
    isLoadingProducts.value = false;
  }
};

const handleAddToCart = async(product: Product) => {
  cartStore.addToCart(product);
}

onMounted(async () => {
  await Promise.all([
    loadBrands(),
    loadCategories(),
    loadProducts()
  ]);
});
</script>

<style scoped>
.product-image {
  height: 200px;
  object-fit: cover;
  object-position: center;
}

.card-title {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  height: 2.4rem;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-name {
  cursor: pointer;
  color: #0d6efd;
}

.product-name:hover {
  text-decoration: underline;
}
</style>

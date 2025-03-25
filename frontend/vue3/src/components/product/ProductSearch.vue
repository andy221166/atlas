<template>
  <div class="search-product px-3 mb-3">
    <ProductSearchForm 
      :brands="brands"
      :categories="categories"
      @search="handleSearch"
    />

    <div v-if="isLoading" class="text-center mt-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <div v-else-if="products.length" class="mt-4 row g-3">
      <div v-for="product in products" :key="product.id" class="col-md-4">
        <ProductCard 
          :product="product"
          @show-detail="showProductDetail"
          @add-to-cart="addToCart"
        />
      </div>
    </div>

    <p v-else class="text-center text-muted mt-3">No products found.</p>

    <ProductDetailModal
      v-model="showModal"
      :product="selectedProduct"
      :is-loading="isLoadingProduct"
      @add-to-cart="addToCart"
    />

    <div
      v-if="totalPages > 1"
      class="d-flex justify-content-center align-items-center mt-4 gap-3"
    >
      <button
        class="btn btn-secondary"
        @click="changePage(currentPage - 1)"
        :disabled="currentPage === 1"
      >
        Previous
      </button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button
        class="btn btn-secondary"
        @click="changePage(currentPage + 1)"
        :disabled="currentPage === totalPages"
      >
        Next
      </button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import ProductCard from './ProductCard.vue'
import ProductDetailModal from './ProductDetailModal.vue'
import ProductSearchForm from './ProductSearchForm.vue'
import { api } from '@/api';

export default {
  components: {
    ProductCard,
    ProductDetailModal,
    ProductSearchForm
  },
  emits: ["addToCart"],
  setup(_, { emit }) {
    const products = ref([]);
    const categories = ref([]);
    const brands = ref([]);
    const currentPage = ref(1);
    const totalPages = ref(1);
    const selectedProduct = ref(null);
    const isLoadingProduct = ref(false);
    const isLoading = ref(false);
    const showModal = ref(false);

    const fetchBrands = async () => {
      try {
        const { data } = await api.products.listBrands();
        brands.value = data.data;
      } catch (error) {
        console.error('Error fetching brands:', error);
      }
    };

    const fetchCategories = async () => {
      try {
        const { data } = await api.products.listCategories();
        categories.value = data.data;
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    const handleSearch = async (filters = {}) => {
      try {
        isLoading.value = true;
        const params = {
          ...filters,
          page: currentPage.value,
          size: 9
        };
        const { data } = await api.products.search(params);
        products.value = data.data;
        totalPages.value = data.pagination.totalPages;
      } catch (error) {
        console.error('Error searching products:', error);
      } finally {
        isLoading.value = false;
      }
    };

    const changePage = (page) => {
      currentPage.value = page;
      handleSearch();
    };

    const showProductDetail = async (product) => {
      try {
        isLoadingProduct.value = true;
        selectedProduct.value = product;
        showModal.value = true;
        const { data } = await api.products.getDetail(product.id);
        selectedProduct.value = data.data;
      } catch (error) {
        console.error('Error fetching product details:', error);
      } finally {
        isLoadingProduct.value = false;
      }
    };

    const addToCart = (product) => {
      emit("addToCart", { ...product, quantity: 1 });
    };

    onMounted(() => {
      fetchBrands();
      fetchCategories();
      handleSearch({
        keyword: '',
        brand_ids: [],
        category_ids: [],
        min_price: null,
        max_price: null
      });
    });

    return {
      products,
      categories,
      brands,
      currentPage,
      totalPages,
      selectedProduct,
      isLoadingProduct,
      isLoading,
      showModal,
      handleSearch,
      addToCart,
      changePage,
      showProductDetail,
    };
  },
};
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

.product-detail-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  object-position: center;
}

.modal-backdrop {
  opacity: 0.5;
}
</style>

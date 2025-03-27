<template>
  <Navbar />
  <div class="container-fluid mt-4">
    <div class="row">
      <!-- Left side: Search and Products -->
      <div class="col-lg-8">
        <div class="search-product px-3 mb-3">
          <ProductSearchForm 
            :brands="brands"
            :categories="categories"
            :search-params="searchParams"
            @search="updateSearchParams"
          />

          <div v-if="products.length" class="mt-4 row g-3">
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
      </div>

      <!-- Right side: Cart and Order Tracking -->
      <div class="col-lg-4">
        <Cart 
          :cart="cart"
          @remove-from-cart="removeFromCart"
          @place-order="placeOrder"
          @update-quantity="updateQuantity"
        />
        <OrderTracking v-if="currentOrderId" :order-id="currentOrderId" />
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from "vue";
import { useStore } from "vuex";
import Navbar from "../components/layout/Navbar.vue";
import ProductCard from "../components/product/ProductCard.vue";
import ProductDetailModal from "../components/product/ProductDetailModal.vue";
import ProductSearchForm from "../components/product/ProductSearchForm.vue";
import Cart from "../components/order/Cart.vue";
import OrderTracking from "../components/order/OrderTracking.vue";
import { api } from '@/api';

export default {
  components: {
    Navbar,
    ProductCard,
    ProductDetailModal,
    ProductSearchForm,
    Cart,
    OrderTracking
  },
  setup() {
    const store = useStore();
    
    // State
    const showModal = ref(false);
    const currentPage = ref(1);
    const pageSize = ref(9);
    const cart = ref([]);
    const currentOrderId = ref(null);
    const products = ref([]);
    const categories = ref([]);
    const brands = ref([]);
    const selectedProduct = ref(null);
    const isLoadingProduct = ref(false);
    const totalPages = ref(1);
    const searchParams = ref({
      keyword: '',
      brand_ids: [],
      category_ids: [],
      min_price: null,
      max_price: null
    });

    // Cart operations
    const addToCart = (product) => {
      // Clear order tracking if cart is empty (starting new cart)
      if (cart.value.length === 0) {
        currentOrderId.value = null;
      }

      const existingItem = cart.value.find(item => item.product.id === product.id);
      if (existingItem) {
        existingItem.quantity += 1;
      } else {
        cart.value.push({ product, quantity: 1 });
      }
      localStorage.setItem('cart', JSON.stringify(cart.value));
    };

    const removeFromCart = (productId) => {
      cart.value = cart.value.filter(item => item.product.id !== productId);
      localStorage.setItem('cart', JSON.stringify(cart.value));
    };

    const updateQuantity = (productId, newQuantity) => {
      const item = cart.value.find(item => item.product.id === productId);
      if (item) {
        item.quantity = newQuantity;
        localStorage.setItem('cart', JSON.stringify(cart.value));
      }
    };

    const placeOrder = async (orderItems) => {
      try {
        const { data } = await api.orders.create({ orderItems });
        if (data.success) {
          cart.value = [];
          localStorage.removeItem('cart');
          currentOrderId.value = data.data.orderId;
        } else {
          alert(data.message || "Failed to place order");
        }
      } catch (error) {
        alert("Failed to place order:", error);
      }
    };

    // Product operations
    const updateSearchParams = async (params) => {
      searchParams.value = { ...searchParams.value, ...params };
      await fetchProducts();
    };

    const changePage = async (page) => {
      currentPage.value = page;
      await fetchProducts();
    };

    const fetchProducts = async (initialParams = null) => {
      try {
        const params = initialParams || {
          ...searchParams.value,
          page: currentPage.value,
          size: pageSize.value
        };

        const requestBody = {
          page: params.page,
          size: params.size,
          ...(params.keyword?.trim() && { keyword: params.keyword.trim() }),
          ...(params.min_price !== null && { minPrice: params.min_price }),
          ...(params.max_price !== null && { maxPrice: params.max_price }),
          ...(params.brand_ids?.length && { brandIds: params.brand_ids.map(Number) }),
          ...(params.category_ids?.length && { categoryIds: params.category_ids.map(Number) })
        };

        const { data } = await api.products.search(requestBody);
        products.value = data.data;
        totalPages.value = data.pagination.totalPages;
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    const fetchBrands = async () => {
      try {
        const { data } = await api.products.listBrands();
        brands.value = data.data.brands;
      } catch (error) {
        console.error('Error fetching brands:', error);
      }
    };

    const fetchCategories = async () => {
      try {
        const { data } = await api.products.listCategories();
        categories.value = data.data.categories;
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    const showProductDetail = (product) => {
      selectedProduct.value = product;
      showModal.value = true;
    };

    onMounted(async () => {
      store.dispatch("user/fetchProfile");
      await Promise.all([fetchBrands(), fetchCategories()]);
      await fetchProducts();
      
      const savedCart = localStorage.getItem('cart');
      if (savedCart) {
        cart.value = JSON.parse(savedCart);
      }
    });

    return {
      products,
      currentPage,
      totalPages,
      selectedProduct,
      isLoadingProduct,
      categories,
      brands,
      searchParams,
      showModal,
      cart,
      currentOrderId,
      updateSearchParams,
      changePage,
      showProductDetail,
      addToCart,
      removeFromCart,
      placeOrder,
      updateQuantity
    };
  },
};
</script>

<style scoped>
.container-fluid {
  max-width: 1800px;
  margin: 0 auto;
  padding: 0 2rem;
}

.product-image {
  height: 180px;
  object-fit: cover;
  object-position: center;
}

.card-title {
  font-size: 1rem;
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

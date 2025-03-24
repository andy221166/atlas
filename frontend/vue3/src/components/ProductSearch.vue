<template>
  <div class="search-product px-3 mb-3">
    <div class="card p-4 shadow-sm">
      <h5 class="card-title mb-3 text-center">Product Search</h5>
      <form @submit.prevent="fetchProducts">
        <input
          v-model="keyword"
          type="text"
          class="form-control mb-3"
          placeholder="Enter keyword"
        />

        <div class="row g-2 mb-3">
          <div class="col-md-6">
            <select v-model="brandId" class="form-select">
              <option value="" disabled>Select Brand</option>
              <option
                v-for="brand in brands"
                :key="brand.id"
                :value="brand.id"
              >
                {{ brand.name }}
              </option>
            </select>
          </div>
          <div class="col-md-6">
            <select v-model="categoryId" class="form-select">
              <option value="" disabled>Select Category</option>
              <option
                v-for="category in categories"
                :key="category.id"
                :value="category.id"
              >
                {{ category.name }}
              </option>
            </select>
          </div>
        </div>

        <div class="row g-2">
          <div class="col-md-6">
            <input
              v-model.number="minPrice"
              type="number"
              class="form-control"
              placeholder="Min Price"
            />
          </div>

          <div class="col-md-6">
            <input
              v-model.number="maxPrice"
              type="number"
              class="form-control"
              placeholder="Max Price"
            />
          </div>
        </div>

        <button type="submit" class="btn btn-primary d-grid mt-4">
          Search
        </button>
      </form>
    </div>

    <div v-if="products.length" class="mt-4 row g-3">
      <div v-for="product in products" :key="product.id" class="col-md-4">
        <div class="card h-100 shadow-sm">
          <img 
            :src="product.imageUrl" 
            class="card-img-top product-image" 
            :alt="product.name"
            @error="handleImageError"
          />
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">{{ product.name }}</h5>
            <h6 class="card-subtitle mb-3 text-muted">
              ${{ product.price.toFixed(2) }}
            </h6>
            <button @click="addToCart(product)" class="btn btn-primary mt-auto">
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </div>

    <p v-else class="text-center text-muted mt-3">No products found.</p>

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
import {listCategoryApi, searchProductApi, listBrandApi} from "@/api/product";
import {onMounted, ref} from "vue";

export default {
  emits: ["addToCart"],
  setup(_, { emit }) {
    const keyword = ref("");
    const categoryId = ref("");
    const brandId = ref("");
    const minPrice = ref(null);
    const maxPrice = ref(null);
    const products = ref([]);
    const categories = ref([]);
    const brands = ref([]);
    const currentPage = ref(1);
    const pageSize = 9;
    const totalPages = ref(1);

    const fetchCategories = async () => {
      try {
        const { data } = await listCategoryApi();
        if (data.success) categories.value = data.data.categories;
      } catch (error) {
        console.error("Failed to fetch categories:", error);
      }
    };

    const fetchBrands = async () => {
      try {
        const { data } = await listBrandApi();
        if (data.success) brands.value = data.data.brands;
      } catch (error) {
        console.error("Failed to fetch brands:", error);
      }
    };

    const fetchProducts = async () => {
      try {
        const params = {
          keyword: keyword.value || undefined,
          category_id: categoryId.value || undefined,
          brand_id: brandId.value || undefined,
          min_price: minPrice.value || undefined,
          max_price: maxPrice.value || undefined,
          page: currentPage.value,
          size: pageSize,
        };
        const { data } = await searchProductApi(params);
        if (data.success) {
          products.value = data.data.products.results;
          totalPages.value = data.data.products.totalPages;
        }
      } catch (error) {
        console.error("Failed to fetch products:", error);
      }
    };

    const addToCart = (product) => {
      emit("addToCart", { ...product, quantity: 1 });
    };

    const changePage = (page) => {
      if (page > 0 && page <= totalPages.value) {
        currentPage.value = page;
        fetchProducts();
      }
    };

    const handleImageError = (event) => {
      event.target.src = require("@/assets/placeholder.png");
    };

    onMounted(() => {
      fetchCategories();
      fetchBrands();
      fetchProducts();
    });

    return {
      keyword,
      categoryId,
      brandId,
      minPrice,
      maxPrice,
      products,
      categories,
      brands,
      currentPage,
      totalPages,
      fetchProducts,
      addToCart,
      changePage,
      handleImageError,
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

.card {
  transition: transform 0.2s;
}

.card:hover {
  transform: translateY(-5px);
}

.card-title {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  height: 2.4rem;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>

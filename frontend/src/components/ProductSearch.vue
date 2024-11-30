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

        <div class="row g-2">
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

          <div class="col-md-3">
            <input
              v-model.number="minPrice"
              type="number"
              class="form-control"
              placeholder="Min Price"
            />
          </div>

          <div class="col-md-3">
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
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">{{ product.name }}</h5>
            <h6 class="card-subtitle mb-2 text-muted">
              ${{ product.price.toFixed(2) }}
            </h6>
            <p
              class="card-text text-truncate"
              style="max-height: 3.6em; overflow: hidden"
            >
              {{ product.description }}
            </p>
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
import {listCategoryApi, searchProductApi} from "@/api/product";
import {onMounted, ref} from "vue";

export default {
  emits: ["addToCart"],
  setup(_, { emit }) {
    const keyword = ref("");
    const categoryId = ref("");
    const minPrice = ref(null);
    const maxPrice = ref(null);
    const products = ref([]);
    const categories = ref([]);
    const currentPage = ref(1);
    const pageSize = 9;
    const totalPages = ref(1);

    const fetchCategories = async () => {
      try {
        const { data } = await listCategoryApi();
        if (data.success) categories.value = data.data;
      } catch (error) {
        console.error("Failed to fetch categories:", error);
      }
    };

    const fetchProducts = async () => {
      try {
        const params = {
          keyword: keyword.value || undefined,
          category_id: categoryId.value || undefined,
          min_price: minPrice.value || undefined,
          max_price: maxPrice.value || undefined,
          page: currentPage.value,
          size: pageSize,
        };
        const { data } = await searchProductApi(params);
        if (data.success) {
          products.value = data.data.records;
          totalPages.value = data.data.totalPages;
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

    onMounted(() => {
      fetchCategories();
      fetchProducts();
    });

    return {
      keyword,
      categoryId,
      minPrice,
      maxPrice,
      products,
      categories,
      currentPage,
      totalPages,
      fetchProducts,
      addToCart,
      changePage,
    };
  },
};
</script>

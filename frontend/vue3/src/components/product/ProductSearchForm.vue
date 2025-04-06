<template>
  <form @submit.prevent="handleSubmit" class="bg-light p-4 rounded-3 mb-4">
    <div class="row g-3">
      <!-- Search Input -->
      <div class="col-md-12">
        <div class="form-group">
          <input
            v-model="form.keyword"
            type="text"
            class="form-control"
            placeholder="Search products..."
          />
        </div>
      </div>

      <!-- Price Range -->
      <div class="col-md-12">
        <div class="row g-3">
          <div class="col-md-6">
            <div class="form-group">
              <input
                v-model="form.min_price"
                type="number"
                class="form-control"
                placeholder="Min Price"
                min="0"
                step="0.01"
              />
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <input
                v-model="form.max_price"
                type="number"
                class="form-control"
                placeholder="Max Price"
                min="0"
                step="0.01"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- Brand and Category Filters -->
      <div class="col-md-12">
        <div class="row g-3">
          <div class="col-md-6">
            <div class="form-group">
              <select v-model="form.brand_ids" class="form-select" multiple>
                <option value="all">All Brands</option>
                <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                  {{ brand.name }}
                </option>
              </select>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <select v-model="form.category_ids" class="form-select" multiple>
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
</template>

<script>
import {ref, watch} from 'vue';

export default {
  props: {
    brands: {
      type: Array,
      default: () => []
    },
    categories: {
      type: Array,
      default: () => []
    },
    searchParams: {
      type: Object,
      default: () => ({})
    },
    page: {
      type: Number,
      default: 1
    },
    size: {
      type: Number,
      default: 9
    }
  },
  emits: ['search'],
  setup(props, { emit }) {
    const form = ref({
      keyword: '',
      brand_ids: [],
      category_ids: [],
      min_price: null,
      max_price: null
    });

    const handleSubmit = () => {
      // Create a clean object with only defined values
      const searchParams = {
        page: props.page,
        size: props.size || 9  // Ensure size is always 9 if not provided
      };

      // Only add parameters that have values
      if (form.value.keyword?.trim()) searchParams.keyword = form.value.keyword.trim();
      
      // Handle brand_ids
      const brandIds = form.value.brand_ids.filter(id => id !== 'all');
      if (brandIds.length > 0) searchParams.brand_ids = brandIds;
      
      // Handle category_ids
      const categoryIds = form.value.category_ids.filter(id => id !== 'all');
      if (categoryIds.length > 0) searchParams.category_ids = categoryIds;
      
      if (form.value.min_price !== null) searchParams.min_price = form.value.min_price;
      if (form.value.max_price !== null) searchParams.max_price = form.value.max_price;

      emit('search', searchParams);
    };

    // Sync with parent search params
    watch(() => props.searchParams, (newParams) => {
      form.value = {
        ...form.value,
        ...newParams
      };
    }, { immediate: true });

    return {
      form,
      handleSubmit
    };
  }
};
</script> 
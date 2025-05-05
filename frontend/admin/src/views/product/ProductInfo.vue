<template>
  <div class="container-fluid p-2">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1 class="h2">Product Information</h1>
      <div class="d-flex gap-2">
        <button class="btn btn-sm btn-outline-primary" @click="handleEdit">
          <i class="bi bi-pencil me-1"></i> Edit
        </button>
        <button class="btn btn-sm btn-outline-danger" @click="handleDelete">
          <i class="bi bi-trash me-1"></i> Delete
        </button>
        <button class="btn btn-outline-secondary" @click="router.back()">
          <i class="bi bi-arrow-left me-1"></i> Back
        </button>
      </div>
    </div>

    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <div v-else-if="product" class="row">
      <!-- Product Basic Information -->
      <div class="col-md-4">
        <div class="card">
          <img :src="getProductImage(product.image)" :alt="product.name" class="card-img-top product-image">
          <div class="card-body">
            <h5 class="card-title">{{ product.name }}</h5>
            <p class="card-text">
              <span class="fs-4 fw-bold">${{ product.price.toFixed(2) }}</span>
            </p>
            <div class="d-flex align-items-center mb-3">
              <span :class="{
                'badge': true,
                'bg-success': product.status === 'IN_STOCK',
                'bg-danger': product.status === 'OUT_STOCK',
                'bg-secondary': product.status === 'DISCONTINUED'
              }">
                {{ product.status }}
              </span>
              <span v-if="product.status === 'IN_STOCK'" class="badge bg-info ms-2">Stock: {{ product.quantity }}</span>
            </div>
            <p class="mb-3"><strong>Brand:</strong> {{ product.brand.name }}</p>
            <p>
              <strong>Categories:</strong>
              <span v-for="category in product.categories" :key="category.id" class="badge bg-secondary ms-1">
                {{ category.name }}
              </span>
            </p>
          </div>
        </div>
      </div>

      <!-- Product Other Information -->
      <div class="col-md-8">
        <!-- Details -->
        <div class="card mb-4">
          <div class="card-body">
            <h5 class="card-title">Description</h5>
            <p class="card-text">{{ product.details.description }}</p>
          </div>
        </div>

        <!-- Attributes -->
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Specifications</h5>
            <div class="table-responsive">
              <table class="table">
                <tbody>
                  <tr v-for="attr in product.attributes" :key="attr.id">
                    <th style="width: 200px">{{ attr.name }}</th>
                    <td>{{ attr.value }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="alert alert-danger">
      Product not found
    </div>
  </div>
</template>

<script setup lang="ts">
import { deleteProduct, getProduct } from '@/services/product/product.service';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();
const loading = ref(true);
const product = ref<any>(null);

const getProductImage = (image: string | null): string => {
  if (!image) {
    // Handle null/empty case
    return new URL('@/assets/product-placeholder.jpg', import.meta.url).href;
  }

  // Handle both URL and base64 cases - they can be used directly in src
  return image;
};

const handleEdit = () => {
  router.push({ name: 'productEdit', params: { 'id': product.value.id } });
};

const handleDelete = async () => {
  if (!product.value) return;

  if (confirm('Are you sure you want to delete this product?')) {
    try {
      await deleteProduct(product.value.id);
      router.push({ name: 'productList' });
    } catch (error) {
      console.error('Error deleting product:', error);
    }
  }
};

onMounted(async () => {
  try {
    const productId = route.params.id as string;
    const response = await getProduct(parseInt(productId));
    if (response.success) {
      product.value = response.data;
    }
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.product-image {
  object-fit: cover;
  height: 250px;
  width: 100%;
  border-top-left-radius: var(--bs-card-border-radius);
  border-top-right-radius: var(--bs-card-border-radius);
}
</style>
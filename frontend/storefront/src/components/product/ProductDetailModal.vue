<template>
  <div v-if="modelValue" class="modal-backdrop show" @click="$emit('update:modelValue', false)"></div>
  <div v-if="modelValue" class="modal show d-block" tabindex="-1">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Product Details</h5>
          <button type="button" class="btn-close" @click="$emit('update:modelValue', false)"></button>
        </div>
        <div class="modal-body">
          <div v-if="isLoading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </div>
          <div v-else class="row">
            <div class="col-md-6">
              <img 
                :src="product.imageUrl" 
                class="img-fluid rounded product-detail-image" 
                :alt="product.name"
                @error="handleImageError"
              />
            </div>
            <div class="col-md-6">
              <h4 class="mb-3">{{ product.name }}</h4>
              <h5 class="text-primary mb-4">${{ product.price.toFixed(2) }}</h5>
              
              <div class="mb-3">
                <h6>Description</h6>
                <p class="text-muted">{{ product.description || 'No description available.' }}</p>
              </div>

              <div v-if="product.attributes" class="mb-1">
                <h6>Attributes</h6>
                <div class="table-responsive">
                  <table class="table table-bordered table-sm">
                    <tbody>
                      <tr v-for="(value, key) in product.attributes" :key="key">
                        <th class="text-muted">{{ key }}</th>
                        <td>{{ value }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              <div class="mb-4">
                <h6>Brand</h6>
                <p>{{ product.brand || 'N/A' }}</p>
              </div>

              <div class="mb-4">
                <h6>Categories</h6>
                <div v-if="product.categories?.length" class="d-flex flex-wrap gap-2">
                  <span 
                    v-for="category in product.categories" 
                    :key="category"
                    class="badge bg-secondary"
                  >
                    {{ category }}
                  </span>
                </div>
                <p v-else class="text-muted">N/A</p>
              </div>

              <div class="d-grid">
                <button @click="$emit('add-to-cart', product)" class="btn btn-primary">
                  Add to Cart
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    modelValue: Boolean,
    product: Object,
    isLoading: Boolean
  },
  emits: ['update:modelValue', 'add-to-cart'],
  methods: {
    handleImageError(event) {
      event.target.src = require("@/assets/product-placeholder.jpg");
    }
  }
}
</script>

<style scoped>
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
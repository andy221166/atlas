<template>
  <div class="card h-100 shadow-sm">
    <img 
      :src="product.imageUrl" 
      class="card-img-top product-image" 
      :alt="product.name"
      @error="handleImageError"
    />
    <div class="card-body d-flex flex-column">
      <h5 class="card-title product-name" @click="$emit('show-detail', product)">
        {{ product.name }}
      </h5>
      <h6 class="card-subtitle mb-3 text-muted">
        ${{ product.price.toFixed(2) }}
      </h6>
      <button @click="$emit('add-to-cart', product)" class="btn btn-primary mt-auto">
        Add to Cart
      </button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    product: {
      type: Object,
      required: true
    }
  },
  emits: ['show-detail', 'add-to-cart'],
  methods: {
    handleImageError(event) {
      event.target.src = require("@/assets/product-placeholder.jpg");
    }
  }
}
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
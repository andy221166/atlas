<template>
  <div class="shopping-cart p-4 border bg-light shadow-sm rounded">
    <h5 class="mb-4 text-center">Your Cart</h5>

    <ul v-if="cart.length" class="list-group mb-3">
      <li
        v-for="item in cart"
        :key="item.product.id"
        class="list-group-item border-0 border-bottom py-3"
      >
        <div class="d-flex align-items-center gap-3">
          <!-- Remove button -->
          <button
            @click="removeFromCart(item.product.id)"
            class="btn btn-link text-danger p-0 fs-5"
            :disabled="isProcessing"
          >
            <i class="bi bi-x"></i>
          </button>

          <!-- Product image -->
          <img 
            :src="item.product.imageUrl" 
            :alt="item.product.name"
            class="cart-item-image"
            @error="handleImageError"
          />

          <!-- Product name -->
          <div class="flex-grow-1">
            <strong>{{ item.product.name }}</strong>
          </div>

          <!-- Quantity controls -->
          <div class="d-flex align-items-center gap-2">
            <button
              @click="decreaseQuantity(item)"
              class="btn btn-sm btn-outline-secondary"
              :disabled="isProcessing"
            >
              -
            </button>
            <input
              type="number"
              v-model="item.quantity"
              class="form-control form-control-sm text-center"
              style="width: 60px"
              min="1"
              @change="updateQuantity(item.product.id, parseInt(item.quantity) || 1)"
              :disabled="isProcessing"
            />
            <button
              @click="increaseQuantity(item)"
              class="btn btn-sm btn-outline-secondary"
              :disabled="isProcessing"
            >
              +
            </button>
          </div>

          <!-- Price -->
          <span class="fw-bold">${{ getItemTotal(item) }}</span>
        </div>
      </li>
    </ul>
    <p v-else class="text-center text-muted">Your cart is empty.</p>

    <div v-if="cart.length" class="d-flex justify-content-between mt-3">
      <span class="fw-bold">Total:</span>
      <span class="fw-bold">${{ cartTotal.toFixed(2) }}</span>
    </div>

    <button
      @click="placeOrder"
      class="btn btn-success w-100 mt-4"
      :disabled="!cart.length || isProcessing"
    >
      {{ isProcessing ? 'Processing...' : 'Place Order' }}
    </button>
  </div>
</template>

<script>
import {computed, ref} from "vue";

export default {
  props: {
    cart: {
      type: Array,
      required: true
    }
  },
  emits: ["orderPlaced", "removeFromCart", "placeOrder", "updateQuantity"],
  setup(props, { emit }) {
    const isProcessing = ref(false);

    const handleImageError = (event) => {
      event.target.src = require("@/assets/product-placeholder.jpg");
    };

    const cartTotal = computed(() => {
      return props.cart.reduce((total, item) => {
        return total + (item.product.price * item.quantity);
      }, 0);
    });
    
    const getItemTotal = (item) => {
      return (item.product.price * item.quantity).toFixed(2);
    };

    const removeFromCart = (productId) => {
      emit("removeFromCart", productId);
    };

    const increaseQuantity = (item) => {
      emit("updateQuantity", item.product.id, item.quantity + 1);
    };

    const decreaseQuantity = (item) => {
      if (item.quantity <= 1) {
        removeFromCart(item.product.id);
      } else {
        emit("updateQuantity", item.product.id, item.quantity - 1);
      }
    };

    const placeOrder = async () => {
      if (!props.cart.length || isProcessing.value) return;

      try {
        isProcessing.value = true;
        const orderItems = props.cart.map((item) => ({
          productId: item.product.id,
          quantity: item.quantity,
        }));

        const orderId = await emit("placeOrder", orderItems);
        emit("orderPlaced", orderId);
      } catch (error) {
        console.error("Failed to place order: " + error.message);
      } finally {
        isProcessing.value = false;
      }
    };

    return {
      cart: computed(() => props.cart),
      cartTotal,
      getItemTotal,
      removeFromCart,
      placeOrder,
      isProcessing,
      increaseQuantity,
      decreaseQuantity,
      handleImageError
    };
  },
};
</script>

<style scoped>
.shopping-cart {
  background-color: #f8f9fa;
  border-radius: 5px;
}

.cart-item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>

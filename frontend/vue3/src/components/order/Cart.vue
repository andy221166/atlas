<template>
  <div class="shopping-cart p-4 border bg-light shadow-sm rounded">
    <h5 class="mb-4 text-center">Your Cart</h5>

    <ul v-if="cart.length" class="list-group mb-3">
      <li
        v-for="item in cart"
        :key="item.product.id"
        class="list-group-item d-flex justify-content-between align-items-center border-0 border-bottom py-2"
      >
        <div>
          <strong>{{ item.product.name }}</strong>
          <small class="text-muted d-block"
            >Quantity: {{ item.quantity }}</small
          >
          <button
            @click="removeFromCart(item.product.id)"
            class="btn btn-sm btn-outline-danger mt-1"
            :disabled="isProcessing"
          >
            Remove
          </button>
        </div>
        <span class="fw-bold">${{ getItemTotal(item) }}</span>
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
import { computed, ref } from "vue";

export default {
  props: {
    cart: {
      type: Array,
      required: true
    }
  },
  emits: ["orderPlaced", "removeFromCart", "placeOrder"],
  setup(props, { emit }) {
    const isProcessing = ref(false);

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
        console.error("Failed to place order:", error);
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
      isProcessing
    };
  },
};
</script>

<style scoped>
.shopping-cart {
  background-color: #f8f9fa;
  border-radius: 5px;
}
</style>

<template>
  <div class="shopping-cart p-4 border bg-light shadow-sm rounded">
    <h5 class="mb-4 text-center">Your Cart</h5>

    <ul v-if="cart.length" class="list-group mb-3">
      <li
        v-for="item in cart"
        :key="item.aggProduct.id"
        class="list-group-item d-flex justify-content-between align-items-center border-0 border-bottom py-2"
      >
        <div>
          <strong>{{ item.aggProduct.name }}</strong>
          <small class="text-muted d-block"
            >Quantity: {{ item.quantity }}</small
          >
          <button
            @click="removeFromCart(item.aggProduct.id)"
            class="btn btn-sm btn-outline-danger mt-1"
          >
            Remove
          </button>
        </div>
        <span class="fw-bold"
          >${{ (item.aggProduct.price * item.quantity).toFixed(2) }}</span
        >
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
      :disabled="!cart.length"
    >
      Place Order
    </button>
  </div>
</template>

<script>
import {computed} from "vue";
import {useStore} from "vuex";

export default {
  emits: ["orderPlaced"],
  setup(_, { emit }) {
    const store = useStore();

    // Make cart and cartTotal reactive
    const cart = computed(() => store.state.cart);
    const cartTotal = computed(() => store.getters.cartTotal);

    const placeOrder = async () => {
      if (!store.state.cart.length) return;

      const orderItems = store.state.cart.map((item) => ({
        productId: item.aggProduct.id,
        quantity: item.quantity,
      }));

      try {
        const response = await store.dispatch("placeOrder", orderItems);
        if (response.success) {
          emit("orderPlaced"); // Emit event after successful orderPayload
        } else {
          alert(`Failed to place orderPayload: ${response.message}`);
        }
      } catch (error) {
        alert("An unexpected error occurred while placing the orderPayload.");
      }
    };

    return {
      cart,
      cartTotal,
      removeFromCart: (productId) => store.commit("removeFromCart", productId),
      placeOrder,
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

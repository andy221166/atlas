<template>
  <Navbar />
  <div class="container mt-4">
    <div class="row">
      <!-- Left Side: Search Form and Results -->
      <div class="col-md-7">
        <ProductSearch @addToCart="addToCart" />
      </div>

      <!-- Right Side: Shopping Cart and Order Tracking -->
      <div class="col-md-5">
        <Cart @orderPlaced="resetOrderTrackingInfo" />
        <OrderTracking class="mt-4" :reset="reset" />
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import ProductSearch from "../components/ProductSearch.vue";
import Cart from "../components/Cart.vue";
import OrderTracking from "../components/OrderTracking.vue";
import {useStore} from "vuex";
import {onMounted} from "vue";

export default {
  components: {
    Navbar,
    ProductSearch,
    Cart,
    OrderTracking,
  },
  setup() {
    const store = useStore();

    const addToCart = (product) => {
      store.commit("addToCart", product);
    };

    onMounted(() => {
      store.dispatch("fetchProfile");
    });

    return {
      addToCart,
    };
  },
};
</script>

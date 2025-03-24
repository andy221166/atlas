<template>
  <Navbar />
  <div class="container mt-4">
    <div class="row">
      <div class="col-12">
        <h2 class="text-center mb-4">Welcome to Atlas Store</h2>
        <ProductSearch @addToCart="addToCart" />
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import ProductSearch from "../components/ProductSearch.vue";
import {useStore} from "vuex";
import {onMounted} from "vue";

export default {
  components: {
    Navbar,
    ProductSearch,
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

<style scoped>
.container {
  max-width: 1200px;
}
</style>

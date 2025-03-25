<template>
  <div class="container py-4">
    <div class="row">
      <div class="col-md-8 mx-auto">
        <Cart @order-placed="handleOrderPlaced" />
        <OrderTracking />
      </div>
    </div>
  </div>
</template>

<script>
import Cart from '@/components/order/Cart.vue';
import OrderTracking from '@/components/order/OrderTracking.vue';

export default {
  components: {
    Cart,
    OrderTracking
  },
  setup() {
    const handleOrderPlaced = () => {
      // The order tracking component will automatically update when the order is placed
      // since it's watching the Vuex store
    };

    return {
      handleOrderPlaced
    };
  }
};
</script> 
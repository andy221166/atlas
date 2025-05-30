<template>
	<div class="container-fluid py-4">
		<div class="row g-4">
			<!-- Left side: Product Search -->
			<div class="col-lg-8">
				<ProductSearch />
			</div>
			<!-- Right side: Cart and Order Tracking -->
			<div class="col-lg-4">
				<div class="sticky-top" style="top: 20px; z-index: 100;">
					<Cart />
					<OrderTracking v-if="currentOrderId" />
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import Cart from '@/components/front/Cart.vue';
import OrderTracking from '@/components/front/OrderTracking.vue';
import ProductSearch from '@/components/front/ProductSearch.vue';
import { useCartStore } from '@/stores/cart.store';
import { computed, onMounted } from "vue";

const cartStore = useCartStore();

const currentOrderId = computed(() => cartStore.currentOrderId);

onMounted(() => {
  cartStore.loadCart()
})
</script>

<style scoped>
@media (max-width: 991.98px) {
  .sticky-top {
    position: relative;
    top: 0 !important;
  }
}
</style>

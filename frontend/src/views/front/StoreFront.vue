<template>
	<div class="container-fluid mt-4">
		<div class="row">
			<!-- Left side: Product Search -->
			<div class="col-lg-8">
				<ProductSearch />
			</div>
			<!-- Right side: Cart and Order Tracking -->
			<div class="col-lg-4">
				<Cart />
				<OrderTracking v-if="currentOrderId" :order-id="currentOrderId" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import { useCartStore } from '@/stores/cart.store';
import { onMounted, ref } from "vue";
import Cart from './Cart.vue';
import OrderTracking from './OrderTracking.vue';
import ProductSearch from './ProductSearch.vue';

const cartStore = useCartStore();

const currentOrderId = ref<number | null>(null);

onMounted(() => {
  cartStore.loadCart()
})
</script>

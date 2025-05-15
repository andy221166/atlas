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
import Cart from '@/features/order/components/front/Cart.vue';
import OrderTracking from '@/features/order/components/front/OrderTracking.vue';
import ProductSearch from '@/features/product/components/front/ProductSearch.vue';
import { ref } from "vue";

const currentOrderId = ref<number | null>(null);
</script>

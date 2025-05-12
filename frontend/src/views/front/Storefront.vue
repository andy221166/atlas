<template>
	<div class="container-fluid mt-4">
		<div class="row">
			<!-- Left side: Product Search -->
			<div class="col-lg-8">
				<ProductSearch @add-to-cart="addToCart" />
			</div>
			<!-- Right side: Cart and Order Tracking -->
			<div class="col-lg-4">
				<Cart :cart="cart" @remove-from-cart="removeFromCart" @place-order="placeOrder"
					@update-quantity="updateQuantity" />
				<OrderTracking v-if="currentOrderId" :order-id="currentOrderId" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import ProductSearch from "./product/ProductSearch.vue";
import { ref } from "vue";
import { useCartStore } from "@/stores/cart.store";
import type { OrderItem } from "@/services/order/order.front.interface";
import { toast } from "vue3-toastify";

const cartStore = useCartStore();
const currentOrderId = ref<number | null>(null);



const placeOrderHandler = async (orderItems: OrderItem[]) => {
  try {
    const { data } = await placeOrder({ orderItems });
    currentOrderId.value = data.orderId;
    cartStore.clearCart();
  } catch (error) {
    toast.error("Failed to place order: " + error);
  }
};

const placeOrder = placeOrderHandler;
</script>
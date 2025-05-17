import type { OrderStatus } from "@/features/order/types/order.interface";
import type { ProductStatus } from "@/features/product/types/product.enum";

export const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString()
}

export const formatCurrency = (value: number): string => {
  return value.toFixed(2);
};

export const formatProductStatusLabel = (status: ProductStatus): string => {
  return status.split('_')
      .map(word => word.charAt(0) + word.slice(1).toLowerCase())
      .join(' ');
};

export const formatOrderStatusLabel = (status: OrderStatus): string => {
  return status
    .split('_')
    .map(word => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

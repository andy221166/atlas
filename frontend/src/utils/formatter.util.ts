import { OrderStatus } from "@/types/order.interface";
import { ProductStatus } from "@/types/product.interface";

export const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString()
}

export const formatCurrency = (value: number): string => {
  return value.toFixed(2);
};

export const getProductStatusBadgeClasses = (status: ProductStatus): string => {
  if (!status) {
    return 'badge bg-primary';
  }
  switch (status) {
    case ProductStatus.IN_STOCK:
      return ' bg-success';
    case ProductStatus.OUT_STOCK:
      return ' bg-danger';
    case ProductStatus.DISCONTINUED:
      return ' bg-secondary';
    default:
      return 'badge bg-primary';
  }
};

export const formatProductStatusLabel = (status: ProductStatus): string => {
  return status.split('_')
      .map(word => word.charAt(0) + word.slice(1).toLowerCase())
      .join(' ');
};

export const getOrderStatusBadgeClasses = (status: OrderStatus): string => {
  if (!status) {
    return 'badge bg-primary';
  }
  switch (status.toUpperCase()) {
    case 'PROCESSING':
      return 'badge bg-warning text-dark';
    case 'CONFIRMED':
      return 'badge bg-success';
    case 'CANCELED':
      return 'badge bg-danger';
    default:
      return 'badge bg-primary';
  }
}

export const formatOrderStatusLabel = (status: OrderStatus): string => {
  return status
    .split('_')
    .map(word => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

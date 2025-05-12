import type { Product } from "./product.interface";
import type { User } from "./user.interface";

export interface Order {
  id: number;
  code: string;
  user?: User;
  orderItems: OrderItem[];
  amount: number;
  status: OrderStatus;
  cancelReason?: string;
  createdAt: string; // Date usually handled as ISO string
}

export interface OrderItem {
  product: Product;
  quantity: number;
}

export enum OrderStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

export interface PlaceOrderRequest {
  orderItems: OrderItem[];
}

export interface PlaceOrderResponse {
  orderId: number;
}

export interface ListOrderFilters {
  id?: number;
  status: OrderStatus | '';
  startDate?: string;
  endDate?: string;
  page: number
  size: number
}

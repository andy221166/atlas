import type {ApiResponse} from "@/services/api.interface.ts";
import apiClient from "@/services/apiClient.ts";

export interface ListOrderFilters {
  id?: number;
  userId?: number;
  status?: OrderStatus;
  startDate?: string;
  endDate?: string;
  page: number
  size: number
}

export interface Order {
  id: number;
  code: string;
  user: User;
  orderItems: OrderItem[];
  amount: number;
  status: OrderStatus;
  cancelReason?: string;
  createdAt: string;
}

export interface User {
  id: number;
  firstName: string;
  lastName: string;
}

export interface OrderItem {
  product: Product;
  quantity: number;
}

export interface Product {
  id: number;
  name: string;
  price: number;
}

export enum OrderStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

export const listOrder = async (filters: ListOrderFilters): Promise<ApiResponse<Product[]>> => {
  // Build query parameters
  const queryParams = new URLSearchParams()
  if (filters.id) queryParams.append('id', filters.id.toString())
  if (filters.userId) queryParams.append('userId', filters.userId.toString())
  if (filters.status) queryParams.append('status', filters.status)
  if (filters.startDate) queryParams.append('startDate', filters.startDate)
  if (filters.endDate) queryParams.append('endDate', filters.endDate)
  queryParams.append('page', filters.page.toString())
  queryParams.append('size', filters.size.toString())

  const response = await apiClient.get('/api/admin/orders', { params: queryParams })
  return response.data
};

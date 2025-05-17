import type { ApiResponse } from "@/services/api.interface";
import apiClient from "@/services/apiClient";
import type { GetOrderStatusResponse, ListOrderFilters, Order, PlaceOrderRequest } from "../types/order.interface";

export const listOrder = async (filters: ListOrderFilters): Promise<ApiResponse<Order[]>> => {
  // Build query parameters
  const queryParams = new URLSearchParams()
  if (filters.orderId) queryParams.append('orderId', filters.orderId.toString())
  if (filters.status) queryParams.append('status', filters.status)
  if (filters.startDate) queryParams.append('startDate', filters.startDate)
  if (filters.endDate) queryParams.append('endDate', filters.endDate)
  queryParams.append('page', filters.page.toString())
  queryParams.append('size', filters.size.toString())

  const response = await apiClient.get('/api/front/orders', { params: queryParams })
  return response.data
};

export const placeOrder = async (data: PlaceOrderRequest): Promise<ApiResponse<number>> => {
  const response = await apiClient.post('/api/front/orders/place', data);
  return response.data;
};

export const getOrderStatus = async (orderId: number): Promise<ApiResponse<GetOrderStatusResponse>> => {
  const response = await apiClient.get(`/api/front/orders/${orderId}/status`);
  return response.data;
}

import type { ApiResponse } from "../api.interface";
import apiClient from "../apiClient";
import type { ListOrderFilters, Order, PlaceOrderRequest, PlaceOrderResponse } from "./order.front.interface";

export const listOrder = async (filters: ListOrderFilters): Promise<ApiResponse<Order[]>> => {
  // Build query parameters
  const queryParams = new URLSearchParams()
  if (filters.id) queryParams.append('id', filters.id.toString())
  if (filters.status) queryParams.append('status', filters.status)
  if (filters.startDate) queryParams.append('startDate', filters.startDate)
  if (filters.endDate) queryParams.append('endDate', filters.endDate)
  queryParams.append('page', filters.page.toString())
  queryParams.append('size', filters.size.toString())

  const response = await apiClient.get('/api/front/orders', { params: queryParams })
  return response.data
};

export const placeOrder = async (data: PlaceOrderRequest): Promise<ApiResponse<PlaceOrderResponse>> => {
  const response = await apiClient.post('/api/front/orders/place', data);
  return response.data;
};

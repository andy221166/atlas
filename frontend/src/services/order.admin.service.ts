import type { ApiResponse } from "@/services/api.interface.ts";
import apiClient from "@/services/apiClient.ts";
import type { ListOrderFilters, Order } from "@/types/order.interface";

export const listOrder = async (filters: ListOrderFilters): Promise<ApiResponse<Order[]>> => {
  // Build query parameters
  const queryParams = new URLSearchParams()
  if (filters.orderId) queryParams.append('orderId', filters.orderId.toString())
  if (filters.userId) queryParams.append('userId', filters.userId.toString())
  if (filters.status) queryParams.append('status', filters.status)
  if (filters.startDate) queryParams.append('startDate', filters.startDate)
  if (filters.endDate) queryParams.append('endDate', filters.endDate)
  queryParams.append('page', filters.page.toString())
  queryParams.append('size', filters.size.toString())

  const response = await apiClient.get('/api/admin/orders', { params: queryParams })
  return response.data
};

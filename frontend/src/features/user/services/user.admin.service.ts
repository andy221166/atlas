import type { ApiResponse } from "@/services/api.interface";
import apiClient from "@/services/apiClient";
import type { ListUserFilters, User } from "../types/user.interface";

export const listUser = async (filters: ListUserFilters): Promise<ApiResponse<User[]>> => {
  // Build query parameters
  const queryParams = new URLSearchParams()
  if (filters.id) queryParams.append('id', filters.id)
  if (filters.keyword) queryParams.append('keyword', filters.keyword)
  queryParams.append('page', filters.page.toString())
  queryParams.append('size', filters.size.toString())

  const response = await apiClient.get('/api/admin/users', { params: queryParams })
  return response.data
};

import type { ApiResponse } from "../base/api.interface"
import apiClient from "../base/apiClient"

export interface Category {
  id: number;
  name: string;
}

export const listCategory = async (): Promise<ApiResponse<Category[]>> => {
  const response = await apiClient.get('/api/common/products/categories');
  return response.data;
}

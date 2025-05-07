import type { ApiResponse } from "../api.interface.ts"
import apiClient from "../apiClient.ts"

export interface Category {
  id: number;
  name: string;
}

export const listCategory = async (): Promise<ApiResponse<Category[]>> => {
  const response = await apiClient.get('/api/common/products/categories');
  return response.data;
}

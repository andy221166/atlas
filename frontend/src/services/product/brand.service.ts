import type { ApiResponse } from '../api.interface.ts'
import apiClient from '../apiClient.ts'

export interface Brand {
  id: number
  name: string
}

export const listBrand = async (): Promise<ApiResponse<Brand[]>> => {
  const response = await apiClient.get('/api/common/products/brands')
  return response.data;
}

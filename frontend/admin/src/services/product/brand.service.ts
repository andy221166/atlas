import type { ApiResponse } from '../base/api.interface'
import apiClient from '../base/apiClient'

export interface Brand {
  id: number
  name: string
}

export const listBrand = async (): Promise<ApiResponse<Brand[]>> => {
  const response = await apiClient.get('/api/common/products/brands')
  return response.data;
}

import type { Brand, Category } from '@/interfaces/product.interface.ts';
import type { ApiResponse } from '../api.interface.ts';
import apiClient from '../apiClient.ts';

export const listBrand = async (): Promise<ApiResponse<Brand[]>> => {
  const response = await apiClient.get('/api/common/products/brands')
  return response.data;
}

export const listCategory = async (): Promise<ApiResponse<Category[]>> => {
  const response = await apiClient.get('/api/common/products/categories');
  return response.data;
}

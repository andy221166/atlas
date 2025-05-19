import type { ApiResponse } from "@/services/api.interface";
import apiClient from "@/services/apiClient";
import type { Brand, Category } from "@/types/product.interface";

export const listBrand = async (): Promise<ApiResponse<Brand[]>> => {
  const response = await apiClient.get('/api/common/products/brands')
  return response.data;
}

export const listCategory = async (): Promise<ApiResponse<Category[]>> => {
  const response = await apiClient.get('/api/common/products/categories');
  return response.data;
}

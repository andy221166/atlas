import type { ApiResponse } from "@/services/api.interface";
import apiClient from "@/services/apiClient";
import type { Product, SearchProductFilters } from "../types/product.interface";

export const searchProduct = async (filters: SearchProductFilters): Promise<ApiResponse<Product[]>> => {
  const queryParams = new URLSearchParams();
  if (filters.keyword) queryParams.append('keyword', filters.keyword);
  if (filters.minPrice) queryParams.append('min_price', filters.minPrice.toString());
  if (filters.maxPrice) queryParams.append('max_price', filters.maxPrice.toString());
  if (filters.brandId) queryParams.append('brand_id', filters.brandId.toString());
  if (filters.categoryIds?.length) queryParams.append('category_ids', filters.categoryIds.join(','));
  queryParams.append('page', filters.page.toString());
  queryParams.append('size', filters.size.toString());
  
  const response = await apiClient.get('/api/front/products', { params: queryParams });
  return response.data;
};

export const getProduct = async (productId: number): Promise<ApiResponse<Product>> => {
  const response = await apiClient.get(`/api/front/products/${productId}`);
  return response.data;
};

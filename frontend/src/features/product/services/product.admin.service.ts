import type { ApiResponse } from "@/services/api.interface";
import apiClient from "@/services/apiClient";
import type { CreateProductRequest, ListProductFilters, Product, UpdateProductRequest } from "../types/product.interface";


export const listProduct = async (filters: ListProductFilters): Promise<ApiResponse<Product[]>> => {
  const queryParams = new URLSearchParams();
  if (filters.id) queryParams.append('id', filters.id.toString());
  if (filters.keyword) queryParams.append('keyword', filters.keyword);
  if (filters.minPrice) queryParams.append('min_price', filters.minPrice.toString());
  if (filters.maxPrice) queryParams.append('max_price', filters.maxPrice.toString());
  if (filters.status) queryParams.append('status', filters.status);
  if (filters.availableFrom) queryParams.append('available_from', filters.availableFrom);
  if (filters.isActive != null) queryParams.append('is_active', filters.isActive.toString());
  if (filters.brandId) queryParams.append('brand_id', filters.brandId.toString());
  if (filters.categoryIds?.length) queryParams.append('category_ids', filters.categoryIds.join(','));
  queryParams.append('page', filters.page.toString());
  queryParams.append('size', filters.size.toString());

  const response = await apiClient.get('/api/admin/products', { params: queryParams });
  return response.data;
}

export const getProduct = async (productId: number): Promise<ApiResponse<Product>> => {
  const response = await apiClient.get(`/api/admin/products/${productId}`);
  return response.data;
};

export const createProduct = async (data: CreateProductRequest): Promise<ApiResponse<Product>> => {
  const response = await apiClient.post('/api/admin/products', data);
  return response.data;
};

export const updateProduct = async (data: UpdateProductRequest): Promise<ApiResponse<Product>> => {
  const response = await apiClient.put(`/api/admin/products/${data.id}`, data);
  return response.data;
};

export const deleteProduct = async (productId: number): Promise<ApiResponse<void>> => {
  const response = await apiClient.delete(`/api/admin/products/${productId}`);
  return response.data;
};

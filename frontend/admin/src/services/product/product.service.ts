import type { ApiResponse } from '../base/api.interface'
import apiClient from '../base/apiClient'
import type { Brand } from './brand.service'
import type { Category } from './category.service'

export interface Product {
  id: number
  name: string
  imageUrl: string
  price: number
  quantity: number
  status: ProductStatus
  availableFrom: string
  isActive: boolean
}

export interface GetProductResponse extends Product {
  brand: Brand;
  details: ProductDetails;
  attributes: ProductAttribute[];
  categories: Category[];
}

export interface ProductDetails {
  description: string;
};

export interface ProductAttribute {
  id: number;
  name: string;
  value: string;
}

export interface ListProductFilters {
  id?: string
  keyword?: string
  minPrice?: number | null
  maxPrice?: number | null
  status?: ProductStatus | ''
  availableFrom?: string
  isActive?: boolean | null
  brandId?: string
  categoryIds?: string[]
  page: number
  size: number
}

export enum ProductStatus {
  IN_STOCK = 'IN_STOCK',
  OUT_STOCK = 'OUT_STOCK',
  DISCONTINUED = 'DISCONTINUED'
}

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

export const getProduct = async (productId: number): Promise<ApiResponse<GetProductResponse>> => {
  const response = await apiClient.get(`/api/admin/products/${productId}`);
  return response.data;
};

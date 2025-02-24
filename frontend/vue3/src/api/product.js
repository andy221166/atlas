import apiClient from "@/api/index";

export const listCategoryApi = async () => apiClient.get('/api/categories');

export const searchProductApi = async (params) =>
  apiClient.get('/api/products/search', {
    params: { ...params }
  });

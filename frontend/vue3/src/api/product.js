import apiClient from "@/api/index";

export const listBrandApi = async () => apiClient.get('/api/master/brands');

export const listCategoryApi = async () => apiClient.get('/api/master/categories');

export const searchProductApi = async (params) =>
  apiClient.get('/api/front/products', {
    params: { ...params }
  });

import apiClient from "@/api/index";

export const placeOrderApi = async (order) => apiClient.post('/api/orders', order);
export const getOrderApi = async (orderId) => apiClient.get(`/api/orders/${orderId}`);
export const listOrderApi = async (params) => apiClient.get('/api/orders', { params });

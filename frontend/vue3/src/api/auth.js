import apiClient from "@/api/index";

export const loginApi = (username, password) => {
    return apiClient.post('/api/auth/login', { username, password });
};

export const logoutApi = async () => apiClient.post('/api/auth/logout');

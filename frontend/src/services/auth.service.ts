import apiClient from "@/services/apiClient";

export const login = async (identifier: string, password: string) => {
  const response = await apiClient.post(`/api/auth/login`, {
    identifier,
    password,
  });
  return response.data;
};

export const logout = async () => {
  const response = await apiClient.post('/api/auth/logout');
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  return response.data;
};

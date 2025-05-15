import apiClient from "@/services/apiClient";

export const login = async (identifier: string, password: string) => {
  const response = await apiClient.post(`/api/auth/login`, {
    identifier,
    password,
  });
  const { data } = response.data;
  localStorage.setItem('accessToken', data.accessToken);
  localStorage.setItem('refreshToken', data.refreshToken);
};

export const logout = async () => {
  const response = await apiClient.post('/api/auth/logout');
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  return response.data;
};

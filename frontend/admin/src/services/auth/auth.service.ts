import apiClient from '../apiClient.ts';

export const login = async (identifier: string, password: string) => {
  const response = await apiClient.post(`/api/auth/login`, {
    identifier,
    password,
  });
  if (response?.data.success) {
    localStorage.setItem('accessToken', response.data.accessToken);
    localStorage.setItem('refreshToken', response.data.refreshToken);
  }
  return response.data;
};

export const logout = async () => {
  const response = await apiClient.post('/api/auth/logout');
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  return response.data;
};

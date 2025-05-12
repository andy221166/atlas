import type { ApiResponse } from '../api.interface';
import apiClient from '../apiClient';
import type { RegisterRequest } from './user.front.interface';

export const register = async (data: RegisterRequest): Promise<ApiResponse<void>> => {
  const response = await apiClient.post('/api/front/users/register', data);
  return response.data;
};

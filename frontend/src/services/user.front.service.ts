import type { ApiResponse } from "@/services/api.interface";
import apiClient from "@/services/apiClient";
import type { RegisterRequest } from "@/types/user.interface";

export const register = async (data: RegisterRequest): Promise<ApiResponse<void>> => {
  const response = await apiClient.post('/api/front/users/register', data);
  return response.data;
};

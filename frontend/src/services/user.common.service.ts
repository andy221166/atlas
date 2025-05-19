import type { ApiResponse } from "@/services/api.interface";
import apiClient from "@/services/apiClient";
import type { User } from "@/types/user.interface";

export const getProfile = async (): Promise<ApiResponse<User>> => {
  const response = await apiClient.get('/api/common/users/profile')
  return response.data
};

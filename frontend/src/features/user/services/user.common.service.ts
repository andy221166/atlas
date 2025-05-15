import type { ApiResponse } from "../api.interface";
import apiClient from "../apiClient";
import type { Profile } from "./user.common.interface";

export const getProfile = async (): Promise<ApiResponse<Profile>> => {
  const response = await apiClient.get('/api/common/users/profile')
  return response.data
};

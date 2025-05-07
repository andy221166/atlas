import type { ApiResponse } from "../api.interface.ts";
import apiClient from "../apiClient.ts";

export interface ListUserFilters {
  id?: string | null
  username?: string
  email?: string
  phoneNumber?: string
  page: number
  size: number
}

export interface User {
  id: number
  username: string
  firstName: string
  lastName: string
  email: string
  phoneNumber: string
  role: string
}

export interface Profile {
  firstName: string
  lastName: string
  role: string
}

export const getProfile = async (): Promise<ApiResponse<Profile>> => {
  const response = await apiClient.get('/api/common/users/profile')
  return response.data
};

export const listUser = async (filters: ListUserFilters): Promise<ApiResponse<User[]>> => {
  // Build query parameters
  const queryParams = new URLSearchParams()
  if (filters.id) queryParams.append('id', filters.id)
  if (filters.username) queryParams.append('username', filters.username)
  if (filters.email) queryParams.append('email', filters.email)
  if (filters.phoneNumber) queryParams.append('phoneNumber', filters.phoneNumber)
  queryParams.append('page', filters.page.toString())
  queryParams.append('size', filters.size.toString())

  const response = await apiClient.get('/api/admin/users', { params: queryParams })
  return response.data
};

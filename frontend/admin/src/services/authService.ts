import api from './api'
import type { LoginRequest, LoginResponse, ApiResponse } from '@/types/auth'

export const authService = {
  async login(credentials: LoginRequest): Promise<ApiResponse<LoginResponse>> {
    const response = await api.post<ApiResponse<LoginResponse>>('/auth/login', credentials)
    return response.data
  }
}

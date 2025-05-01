import type { LoginRequest, LoginResponse } from '@/services/interfaces/auth.interface'
import api from './api'
import type { ApiResponseWrapper } from './interfaces/common.interface'

export const authService = {
  async login(credentials: LoginRequest): Promise<ApiResponseWrapper<LoginResponse>> {
    const response = await api.post<ApiResponseWrapper<LoginResponse>>('/api/auth/login', credentials)
    return response.data
  },

  async logout(): Promise<ApiResponseWrapper<null>> {
    const response = await api.post<ApiResponseWrapper<null>>('/api/auth/logout')
    return response.data
  }
}

export interface LoginRequest {
  identifier: string,
  password: string,
  deviceId: string
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
}

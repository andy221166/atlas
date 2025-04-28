export interface ApiResponseWrapper<T> {
  success: boolean
  data?: T
  errorCode?: number
  errorMessage?: string
}

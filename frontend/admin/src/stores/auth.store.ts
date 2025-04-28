import api from '@/services/api'
import { authService } from '@/services/auth.service'
import type { LoginRequest } from '@/services/interfaces/auth.interface'
import Cookies from 'js-cookie'
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

// Function to generate a simple device ID (UUID-like)
const generateDeviceId = (): string => {
  const timestamp = Date.now().toString(36)
  const randomStr = Math.random().toString(36).substring(2, 10)
  return `${timestamp}-${randomStr}`
}

// Get or create device ID
const getDeviceId = (): string => {
  let deviceId = Cookies.get('deviceId')
  if (!deviceId) {
    deviceId = generateDeviceId()
    Cookies.set('deviceId', deviceId, { expires: 365, secure: true, sameSite: 'Strict' }) // Store for 1 year
  }
  return deviceId
}

export const useAuthStore = defineStore('auth', () => {
  const router = useRouter()
  
  // Initialize tokens from cookies
  const accessToken = ref<string | null>(Cookies.get('accessToken') || null)
  const refreshToken = ref<string | null>(Cookies.get('refreshToken') || null)
  const isAuthenticated = ref(!!accessToken.value)
  const error = ref<string | null>(null)
  const loading = ref(false)

  const login = async (credentials: Omit<LoginRequest, 'deviceId'>) => {
    try {
      loading.value = true
      error.value = null

      // Create full LoginRequest with deviceId
      const request: LoginRequest = {
        ...credentials,
        deviceId: getDeviceId(),
      }

      const response = await authService.login(request)

      if (response.success && response.data) {
        // Store tokens in cookies
        accessToken.value = response.data.accessToken
        refreshToken.value = response.data.refreshToken
        Cookies.set('accessToken', response.data.accessToken, { expires: 1, secure: true, sameSite: 'Strict' }) // Expires in 1 day
        Cookies.set('refreshToken', response.data.refreshToken, { expires: 7, secure: true, sameSite: 'Strict' }) // Expires in 7 days

        isAuthenticated.value = true

        // Update axios default headers
        api.defaults.headers.common['Authorization'] = `Bearer ${response.data.accessToken}`

        await router.push('/')
      } else {
        error.value = response.errorMessage || 'Login failed'
      }
    } catch (err) {
      error.value = 'An error occurred during login'
      console.error('Login error:', err)
    } finally {
      loading.value = false
    }
  }

  const logout = () => {
    // Clear tokens from cookies
    Cookies.remove('accessToken')
    Cookies.remove('refreshToken')

    accessToken.value = null
    refreshToken.value = null
    isAuthenticated.value = false

    // Clear axios headers
    delete api.defaults.headers.common['Authorization']

    router.push('/login')
  }

  return {
    accessToken,
    refreshToken,
    isAuthenticated,
    error,
    loading,
    login,
    logout,
  }
})

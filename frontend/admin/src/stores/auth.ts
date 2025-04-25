import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authService } from '@/services/authService'
import type { LoginRequest } from '@/types/auth'
import { useRouter } from 'vue-router'

export const useAuthStore = defineStore('auth', () => {
  const router = useRouter()
  const accessToken = ref<string | null>(localStorage.getItem('accessToken'))
  const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))
  const isAuthenticated = ref(!!accessToken.value)
  const error = ref<string | null>(null)
  const loading = ref(false)

  const login = async (credentials: LoginRequest) => {
    try {
      loading.value = true
      error.value = null
      const response = await authService.login(credentials)
      
      if (response.success && response.data) {
        accessToken.value = response.data.accessToken
        refreshToken.value = response.data.refreshToken
        localStorage.setItem('accessToken', response.data.accessToken)
        localStorage.setItem('refreshToken', response.data.refreshToken)
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
    accessToken.value = null
    refreshToken.value = null
    isAuthenticated.value = false
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    router.push('/login')
  }

  return {
    accessToken,
    refreshToken,
    isAuthenticated,
    error,
    loading,
    login,
    logout
  }
})

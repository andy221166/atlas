<template>
  <div class="d-flex align-items-center justify-content-center min-vh-100 bg-light">
    <div class="bg-white p-5 rounded shadow w-md-50 w-100" style="max-width: 450px;">
      <h4 class="text-center mb-4">Login</h4>
      <div v-if="successMessage" class="alert alert-success" role="alert">
        {{ successMessage }}
      </div>
      <div v-if="errorMessage" class="alert alert-danger" role="alert">
        {{ errorMessage }}
      </div>
      <form @submit.prevent="handleLogin">
        <div class="mb-3">
          <label class="form-label">Username</label>
          <input v-model="username" type="text" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label">Password</label>
          <input v-model="password" type="password" class="form-control" required />
        </div>
        <button type="submit" class="btn btn-primary w-100">
          Login
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user.store'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import type { LoginRequest } from '@/interfaces/user.interface'

const username = ref('')
const password = ref('')
const userStore = useUserStore()
const router = useRouter()

const handleLogin = async () => {
  const credentials: LoginRequest = {
    username: username.value,
    password: password.value
  }
  
  const response = await userStore.login(credentials)
  if (response.success) {
    router.push('/')
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

// Direct access to reactive properties
userStore.isAuthenticated
userStore.isAdmin
userStore.user
userStore.loading
</script>

<style scoped>
.w-md-50 {
  @media (min-width: 768px) {
    width: 50% !important;
  }
}
</style>

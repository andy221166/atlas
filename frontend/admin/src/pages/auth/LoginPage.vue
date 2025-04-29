<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">Sign in to your account</h2>

      <el-form :model="form" @submit.native.prevent="handleSubmit" label-position="top">
        <el-form-item label="Identifier">
          <el-input v-model="form.identifier" placeholder="Username, email, or phone number" />
        </el-form-item>

        <el-form-item label="Password">
          <el-input v-model="form.password" type="password" placeholder="Password" show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="authStore.loading" class="w-full">
            {{ authStore.loading ? 'Signing in...' : 'Sign in' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useAuthStore } from '@/stores/auth.store'

const authStore = useAuthStore()

const form = reactive({
  identifier: '',
  password: ''
})

const handleSubmit = async () => {
  await authStore.login(form)
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: #f5f5f5;
  padding: 1rem;
  box-sizing: border-box;
}

.login-card {
  background: #fff;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.login-title {
  text-align: center;
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
}
</style>

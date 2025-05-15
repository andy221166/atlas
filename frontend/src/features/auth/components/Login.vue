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
import { getProfile } from '@/services/user/user.admin.service';
import { useUserStore } from '@/stores/user.store';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../../../services/auth/auth.service';

const username = ref<string>('');
const password = ref<string>('');
const successMessage = ref<string>('');
const errorMessage = ref<string>('');

const userStore = useUserStore();
const router = useRouter();

const handleLogin = async () => {
  try {
    const loginResponse = await login(username.value, password.value);
    // Store tokens first
    userStore.setTokens(loginResponse.data.accessToken, loginResponse.data.refreshToken);

    // Fetch profile
    try {
      const profileResponse = await getProfile();
      successMessage.value = 'You logged in successfully!';
      userStore.setProfile(profileResponse.data);
      setTimeout(() => {
        router.push({ name: 'home' });
      }, 1000);
    } catch (error) {
      userStore.clearAuth();
      errorMessage.value = 'Failed to fetch profile. Please try again.';
    }
  } catch (error) {
    userStore.clearAuth();
    errorMessage.value = 'Login request failed. Please try again.';
  }
};
</script>

<style scoped>
.w-md-50 {
  @media (min-width: 768px) {
    width: 50% !important;
  }
}
</style>

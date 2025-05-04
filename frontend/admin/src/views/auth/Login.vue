<template>
  <div class="d-flex align-items-center justify-content-center min-vh-100 bg-light">
    <div class="bg-white p-5 rounded shadow w-md-50 w-100" style="max-width: 450px;">
      <h3 class="text-center mb-4">Administrator Login</h3>
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
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../../services/auth/auth.service';
import { useUserStore } from '@/stores/user.store';
import { toast } from 'vue3-toastify';
import { getProfile } from '@/services/user/user.service';

const username = ref('');
const password = ref('');
const userStore = useUserStore();
const router = useRouter();

const handleLogin = async () => {
  try {
    const loginResponse = await login(username.value, password.value);
    if (loginResponse.success) {
      // Store tokens first
      userStore.setTokens(loginResponse.data.accessToken, loginResponse.data.refreshToken);

      // Fetch profile
      const profileResponse = await getProfile();
      if (profileResponse.success) {
        if (profileResponse.data.role === 'ADMIN') {
          userStore.setProfile(profileResponse.data); // Store profile data
          router.push('/dashboard');
        } else {
          userStore.clearAuth(); // Clear everything if not admin
          toast.error('Access denied: You do not have the required permissions.');
        }
      } else {
        userStore.clearAuth();
        toast.error('Failed to fetch profile: ' + profileResponse.errorMessage);
      }
    } else {
      toast.error('Login failed: ' + loginResponse.errorMessage);
    }
  } catch (error) {
    userStore.clearAuth();
    toast.error('Login request failed. Please try again.');
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

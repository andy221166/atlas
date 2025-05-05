<template>
  <nav class="navbar navbar-dark" style="background-color: #59359a;">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Atlas Admin</a>
      <div class="d-flex align-items-center">
        <span class="text-light me-3">Welcome, {{ userStore.fullName }}</span>
        <button @click="handleLogout" class="btn btn-outline-light btn-sm">Logout</button>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user.store'
import { useRouter } from 'vue-router'
import { logout } from '@/services/auth/auth.service'

const userStore = useUserStore()
const router = useRouter()

const handleLogout = async () => {
  await logout();
  userStore.clearAuth();
  router.push({ name: 'login' });
}
</script>

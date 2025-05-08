<template>
  <nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/">Atlas</router-link>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link to="/order-history" class="nav-link">Order History</router-link>
          </li>
        </ul>
        <ul class="navbar-nav ms-auto d-flex align-items-center">
          <li v-if="userStore.isAuthenticated" class="nav-item me-3">
            <span class="text-light me-3">Welcome, {{ userStore.fullName }}</span>
          </li>
          <li v-if="userStore.isAuthenticated" class="nav-item">
            <button class="btn btn-sm btn-danger" @click="handleLogout">Logout</button>
          </li>
          <li v-else class="nav-item">
            <router-link to="/register" class="btn btn-sm btn-primary">Register</router-link>
          </li>
        </ul>
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
  router.push({ name: 'home' });
}
</script>

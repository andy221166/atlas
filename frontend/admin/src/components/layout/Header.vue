<template>
  <nav class="navbar">
    <div class="navbar-left">
      <router-link to="/" class="brand-link">Atlas Admin</router-link>
    </div>
    <div class="navbar-right">
      <span v-if="profile" class="welcome-text">
        Welcome {{ profile.firstName }} {{ profile.lastName }}
      </span>
      <button @click="handleLogout" class="logout-btn">Logout</button>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { authService } from '@/services/auth.service'
import type { Profile } from '@/services/interfaces/user.interface'
import { userService } from '@/services/user.service'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const profile = ref<Profile | null>(null)

const fetchProfile = async () => {
  try {
    const response = await userService.fetchProfile()
    if (response.success && response.data) {
      profile.value = response.data;
    }
  } catch (error) {
    console.error('Failed to fetch profile:', error)
  }
}

const handleLogout = async () => {
  try {
    await authService.logout()
    // After successful logout, redirect to login page
    router.push('/login')
  } catch (error) {
    console.error('Failed to logout:', error)
  }
}

onMounted(() => {
  fetchProfile()
})
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.navbar-left {
  display: flex;
  align-items: center;
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.brand-link {
  font-size: 1.25rem;
  font-weight: bold;
  color: #333;
  text-decoration: none;
}

.brand-link:hover {
  color: #666;
}

.welcome-text {
  color: #666;
}

.logout-btn {
  padding: 0.5rem 1rem;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.875rem;
}

.logout-btn:hover {
  background-color: #c82333;
}
</style>

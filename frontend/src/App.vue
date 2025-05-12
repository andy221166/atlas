<template>
  <div class="min-vh-100 d-flex flex-column">
    <!-- Authenticated Layout -->
    <template v-if="userStore.isAuthenticated && !isAuthRoute">
      <NavBar />
      <div class="flex-grow-1 d-flex">
        <Sidebar />
        <div class="flex-grow-1 p-4 bg-light">
          <router-view />
        </div>
      </div>
    </template>

    <!-- Auth Layout (Login/Register) -->
    <template v-else>
      <div class="flex-grow-1">
        <router-view />
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from './stores/user.store'
import NavBar from './components/NavBar.vue'
import Sidebar from './components/Sidebar.vue'

const userStore = useUserStore()
const route = useRoute()

// Check if current route is an auth route (login or register)
const isAuthRoute = computed(() => {
  return ['/login', '/register'].includes(route.path)
})
</script>

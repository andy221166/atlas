<template>
  <div class="min-vh-100 d-flex flex-column">
    <!-- Navbar -->
    <NavBar v-if="userStore.isAuthenticated" />

    <!-- Main content area with sidebar -->
    <div class="flex-grow-1 d-flex">
      <!-- Sidebar -->
      <Sidebar v-if="userStore.isAuthenticated" />

      <!-- Main content -->
      <div class="flex-grow-1 p-4 bg-light">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from './stores/user.store'
import NavBar from './components/NavBar.vue';
import Sidebar from './components/Sidebar.vue';

const userStore = useUserStore()
</script>

<template>
  <div>
    <NavBar />
    <main><router-view /></main>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user.store';
import { onMounted } from 'vue';
import NavBar from './NavBar.vue';

const userStore = useUserStore();

onMounted(async () => {
  // Only try to fetch profile if we have an access token
  if (userStore.isAuthenticated) {
    await userStore.fetchProfile();
  }
});
</script>

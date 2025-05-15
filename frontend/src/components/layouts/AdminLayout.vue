<template>
  <div class="admin-layout">
    <NavBar />
    <div class="admin-body">
      <Sidebar />
      <main><router-view /></main>
    </div>
  </div>
</template>

<script setup lang="ts">
import NavBar from '../common/NavBar.vue';
</script>

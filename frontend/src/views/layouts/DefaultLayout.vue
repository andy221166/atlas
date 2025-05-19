<template>
  <div>
    <NavBar />
    <main><router-view /></main>
  </div>
</template>

<script setup lang="ts">
import NavBar from './NavBar.vue';
</script>

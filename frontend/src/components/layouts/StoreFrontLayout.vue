<template>
  <div>
    <NavBar />
    <main><router-view /></main>
  </div>
</template>

<script setup lang="ts">
import NavBar from '../common/NavBar.vue';
</script>

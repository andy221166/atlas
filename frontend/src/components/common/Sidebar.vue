<template>
  <div class="custom-sidebar text-white" style="width: 250px; min-height: calc(100vh - 56px);">
    <div class="p-3">
      <ul class="nav flex-column">
        <li class="nav-item">
          <router-link to="/dashboard" class="nav-link text-white">
            <i class="bi bi-speedometer2 me-2"></i>Dashboard
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/user" class="nav-link text-white">
            <i class="bi bi-people me-2"></i>User Management
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/product" class="nav-link text-white">
            <i class="bi bi-box-seam me-2"></i>Product Management
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/order" class="nav-link text-white">
            <i class="bi bi-cart-check me-2"></i>Order Management
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
// No script needed for now
</script>

<style scoped>
.custom-sidebar {
  background-color: var(--bs-purple);
  box-shadow: 2px 0 4px rgba(0, 0, 0, 0.1);
}
</style>

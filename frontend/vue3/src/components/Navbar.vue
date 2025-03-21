<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Atlas</a>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link to="/storefront" class="nav-link">Storefront</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/orderPayload-history" class="nav-link">Order History</router-link>
          </li>
        </ul>
        <ul class="navbar-nav ms-auto d-flex align-items-center">
          <li class="nav-item me-3">
            <span class="nav-link">Welcome, {{ profile?.firstName }} {{ profile?.lastName }}</span>
          </li>
          <li class="nav-item me-3">
            <span class="nav-link">Credit: ${{ profile?.credit }}</span>
          </li>
          <li class="nav-item">
            <button class="btn btn-sm btn-danger" @click="logout">Logout</button>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import {useStore} from 'vuex';
import {computed} from 'vue';
import {logoutApi} from '@/api/auth';

export default {
  setup() {
    const store = useStore();
    const profile = computed(() => store.state.profile);

    const logout = async () => {
      try {
        await logoutApi();
        localStorage.removeItem('accessToken');
        window.location.href = '/storefront';
      } catch (error) {
        console.error('Sign out failed');
      }
    };

    return {
      profile,
      logout,
    };
  },
};
</script>

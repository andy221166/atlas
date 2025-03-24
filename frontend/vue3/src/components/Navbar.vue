<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/">Atlas</router-link>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link to="/order-history" class="nav-link">Order History</router-link>
          </li>
        </ul>
        <ul class="navbar-nav ms-auto d-flex align-items-center">
          <li class="nav-item me-3">
            <span class="nav-link">Welcome, {{ profile?.firstName }} {{ profile?.lastName }}</span>
          </li>
          <li class="nav-item me-3">
            <router-link to="/checkout" class="nav-link position-relative">
              <i class="bi bi-cart3 fs-5"></i>
              <span class="position-absolute top-10 start-100 translate-middle badge rounded-pill bg-danger">
                {{ cartItemCount }}
              </span>
            </router-link>
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
    const cartItemCount = computed(() => store.state.cart.reduce((total, item) => total + item.quantity, 0));

    const logout = async () => {
      try {
        await logoutApi();
        localStorage.removeItem('accessToken');
        window.location.href = '/';
      } catch (error) {
        console.error('Logout failed');
      }
    };

    return {
      profile,
      cartItemCount,
      logout,
    };
  },
};
</script>

<style>
@import 'bootstrap-icons/font/bootstrap-icons.css';
</style>

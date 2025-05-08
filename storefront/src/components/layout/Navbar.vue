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
import {api} from '@/api';
import {toast} from "vue3-toastify";

export default {
  setup() {
    const store = useStore();
    const profile = computed(() => store.state.user.profile);

    const logout = async () => {
      try {
        const { data } = await api.auth.logout();
        if (data.success) {
          localStorage.removeItem('accessToken');
          window.location.href = '/';
        } else {
          toast.error(data.message);
        }
      } catch (error) {
        toast.error('Logout failed: ' + error.message);
      }
    };

    return {
      profile,
      logout,
    };
  },
};
</script>

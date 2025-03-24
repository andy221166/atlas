<template>
  <div class="container mt-5 d-flex justify-content-center">
    <div class="form-container">
      <h4 class="text-center mb-2">Login</h4>
      <Alert v-if="errorMessage" :message="errorMessage" type="danger" />
      <form @submit.prevent="login">
        <FormField label="Username" v-model="username" type="text" />
        <FormField label="Password" v-model="password" type="password" />
        <div class="mt-3">
          <button type="submit" class="btn btn-primary">Login</button>
          <button type="button" class="btn btn-outline-primary ms-2" @click="goToRegister">Register</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import {loginApi} from '@/api/auth';
import Alert from '@/components/Alert.vue';
import FormField from '@/components/FormField.vue';

export default {
  name: 'Login',
  components: { Alert, FormField },
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    }
  },
  methods: {
    async login() {
      try {
        const { data } = await loginApi(this.username, this.password);
        if (data.success) {
          localStorage.setItem('accessToken', data.data.accessToken);
          this.$router.push('/');
        } else {
          this.setError(data.code, data.message); // Handle known error format from server response
        }
      } catch (error) {
        if (error.response) {
          if (error.response.data && error.response.data.code && error.response.data.message) {
            this.setError(error.response.data.code, error.response.data.message);
          } else {
            this.setError(error.response.status, "An unexpected server error occurred. Please try again.");
          }
        } else {
          this.setError("Network Error", "Unable to connect. Please check your network connection.");
        }
      }
    },
    goToRegister() {
      this.$router.push('/register');
    },
    setError(code, message) {
      if (code) {
        this.errorMessage = `ERR ${code}: ${message}`;
      } else {
        this.errorMessage = `${message}`;
      }
    }
  }
}
</script>

<style scoped>
.form-container {
  width: 100%;
  max-width: 400px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
}
</style>

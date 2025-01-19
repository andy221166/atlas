<template>
  <div class="container mt-5 d-flex justify-content-center">
    <div class="form-container">
      <h4 class="text-center mb-2">Sign In</h4>
      <Alert v-if="errorMessage" :message="errorMessage" type="danger" />
      <form @submit.prevent="signIn">
        <FormField label="Username" v-model="username" type="text" />
        <FormField label="Password" v-model="password" type="password" />
        <div class="mt-3">
          <button type="submit" class="btn btn-primary">Sign In</button>
          <button type="button" class="btn btn-outline-primary ms-2" @click="goToSignUp">Sign Up</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import {signInApi} from '@/api/user';
import Alert from '@/components/Alert.vue';
import FormField from '@/components/FormField.vue';

export default {
  name: 'SignIn',
  components: { Alert, FormField },
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    }
  },
  methods: {
    async signIn() {
      try {
        const { data } = await signInApi(this.username, this.password);
        if (data.success) {
          localStorage.setItem('accessToken', data.data.accessToken);
          this.$router.push('/storefront');
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
    goToSignUp() {
      this.$router.push('/sign-up');
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

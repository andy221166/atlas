<template>
  <div class="container mt-5 d-flex justify-content-center">
    <div class="form-container">
      <h4 class="text-center mb-2">Sign Up</h4>
      <Alert v-if="successMessage" :message="successMessage" type="success" />
      <Alert v-if="errorMessage" :message="errorMessage" type="danger" />
      <form @submit.prevent="signUp">
        <FormField label="Username" v-model="username" type="text" required />
        <FormField label="First Name" v-model="firstName" type="text" required />
        <FormField label="Last Name" v-model="lastName" type="text" required />
        <FormField label="Email address" v-model="email" type="email" required />
        <FormField label="Phone Number" v-model="phoneNumber" type="text" required />
        <FormField label="Password" v-model="password" type="password" required />
        <FormField label="Confirm Password" v-model="confirmPassword" type="password" required />
        <div class="mt-3">
          <button type="submit" class="btn btn-primary">Sign Up</button>
          <button type="button" class="btn btn-outline-primary ms-2" @click="goToSignIn">Sign In</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import {signUpApi} from '@/api/user';
import Alert from '@/components/Alert.vue';
import FormField from '@/components/FormField.vue';

export default {
  name: 'SignUp',
  components: { Alert, FormField },
  data() {
    return {
      username: '',
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      password: '',
      confirmPassword: '',
      successMessage: '',
      errorMessage: ''
    }
  },
  methods: {
    async signUp() {
      if (this.password !== this.confirmPassword) {
        this.errorMessage = 'Passwords do not match';
        return;
      }

      try {
        const { data } = await signUpApi(this.username, this.password, this.firstName, this.lastName, this.email, this.phoneNumber);
        if (data.success) {
          this.successMessage = 'You signed up successfully';
          this.resetForm();
          setTimeout(this.goToSignIn, 1000);
        } else {
          this.setError(data.code, data.message);
        }
      } catch (error) {
        // Handle 500 error with response data
        if (error.response && error.response.data && error.response.data.code) {
          // Server returned a known error format with code and message
          this.setError(error.response.data.code, error.response.data.message);
        } else if (error.response && error.response.status === 500) {
          // Handle unknown 500 error without custom message
          this.setError('Server Error', 'An unexpected server error occurred. Please try again later.');
        } else {
          // Handle network or unexpected errors without a response
          this.setError('Network Error', 'An unexpected error occurred. Please check your network connection.');
        }
      }
    },
    goToSignIn() {
      this.$router.push('/sign-in');
    },
    resetForm() {
      this.username = '';
      this.firstName = '';
      this.lastName = '';
      this.email = '';
      this.phoneNumber = '';
      this.password = '';
      this.confirmPassword = '';
      this.errorMessage = '';
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
  max-width: 500px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
}
</style>

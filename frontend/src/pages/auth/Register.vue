<template>
  <div class="container mt-5 d-flex justify-content-center">
    <div class="form-container">
      <h4 class="text-center mb-4">Register</h4>
      <div v-if="successMessage" class="alert alert-success" role="alert">
        {{ successMessage }}
      </div>
      <div v-if="errorMessage" class="alert alert-danger" role="alert">
        {{ errorMessage }}
      </div>
      <form @submit.prevent="submit">
        <div class="mb-3">
          <label for="username" class="form-label">Username</label>
          <input id="username" v-model="username" type="text" class="form-control" required />
        </div>
        <div class="mb-3">
          <label for="firstName" class="form-label">First Name</label>
          <input id="firstName" v-model="firstName" type="text" class="form-control" required />
        </div>
        <div class="mb-3">
          <label for="lastName" class="form-label">Last Name</label>
          <input id="lastName" v-model="lastName" type="text" class="form-control" required />
        </div>
        <div class="mb-3">
          <label for="email" class="form-label">Email address</label>
          <input id="email" v-model="email" type="email" class="form-control" required />
        </div>
        <div class="mb-3">
          <label for="phoneNumber" class="form-label">Phone Number</label>
          <input id="phoneNumber" v-model="phoneNumber" type="text" class="form-control" required />
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input id="password" v-model="password" type="password" class="form-control" required />
        </div>
        <div class="mb-3">
          <label for="confirmPassword" class="form-label">Confirm Password</label>
          <input id="confirmPassword" v-model="confirmPassword" type="password" class="form-control" required />
        </div>
        <div class="mt-3 d-flex gap-2">
          <button type="submit" class="btn btn-primary">Register</button>
          <button type="button" class="btn btn-outline-primary" @click="goToLogin">Login</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { register } from '@/services/user.front.service';
import type { RegisterRequest } from '@/interfaces/user.interface';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// Form state
const username = ref<string>('');
const firstName = ref<string>('');
const lastName = ref<string>('');
const email = ref<string>('');
const phoneNumber = ref<string>('');
const password = ref<string>('');
const confirmPassword = ref<string>('');
const successMessage = ref<string>('');
const errorMessage = ref<string>('');

// API call to register
const submit = async () => {
  if (password.value !== confirmPassword.value) {
    errorMessage.value = 'Passwords do not match';
    return;
  }

  const request: RegisterRequest = {
    username: username.value,
    firstName: firstName.value,
    lastName: lastName.value,
    email: email.value,
    phoneNumber: phoneNumber.value,
    password: password.value,
  };

  try {
    await register(request);
    successMessage.value = 'You registered successfully';
    resetForm();
    setTimeout(goToLogin, 1000);
  } catch (error) {
    errorMessage.value = 'Registration request failed: ' + error;
  }
};

const goToLogin = () => {
  router.push({ name: 'login' });
};

// Reset form fields
const resetForm = () => {
  username.value = '';
  firstName.value = '';
  lastName.value = '';
  email.value = '';
  phoneNumber.value = '';
  password.value = '';
  confirmPassword.value = '';
};  
</script>

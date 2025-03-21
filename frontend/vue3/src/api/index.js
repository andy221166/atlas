// axios/index.js
import axios from 'axios';
import router from '@/router'; // Import router to handle redirection

const apiClient = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add Authorization header if token is present
apiClient.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem('accessToken');
  if (accessToken) {
    config.headers['Authorization'] = `Bearer ${accessToken}`;
  }
  return config;
});

// Response interceptor to handle 401 Unauthorized errors
apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('accessToken'); // Optional: clear token if unauthorized
      router.push('/login'); // Redirect to Login page
    }
    return Promise.reject(error);
  }
);

export default apiClient;

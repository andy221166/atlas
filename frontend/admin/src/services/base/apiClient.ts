// src/services/apiClient.ts
import router from '@/router';
import { generateDeviceId } from '@/utils/deviceIdGenerator';
import axios, { AxiosError } from 'axios';
import { toast } from 'vue3-toastify';
import type { ApiResponse } from './api.interface';

const API_URL = import.meta.env.VITE_API_URL;
const DEVICE_ID_HEADER = 'X-Device-Id';

const apiClient = axios.create({
  baseURL: API_URL,
  timeout: 30000, // 30 seconds timeout
  headers: {
    'Content-Type': 'application/json'
  },
  // Add withCredentials to handle CORS
  withCredentials: true
});

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    console.log('Request interceptor triggered');
    config.headers = config.headers || {};

    // Add deviceId header
    const deviceId = getOrCreateDeviceId();
    config.headers[DEVICE_ID_HEADER] = deviceId;

    // Add access token
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error) => {
    console.error('Request interceptor error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
apiClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError<ApiResponse<any>>) => {
    const originalRequest = error.config;

    // Handle 401 Unauthorized
    if (error.response?.status === 401) {
      // If refresh token exists, try to refresh
      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken && originalRequest) {
        try {
          // Call your refresh token endpoint
          const refreshTokenResponse = await axios.post(`${API_URL}/api/auth/refresh-token`, {
            refreshToken
          }, {
            headers: {
              [DEVICE_ID_HEADER]: getOrCreateDeviceId()
            }
          });

          if (refreshTokenResponse.data.success) {
            // Update tokens
            localStorage.setItem('accessToken', refreshTokenResponse.data.data.accessToken);
            localStorage.setItem('refreshToken', refreshTokenResponse.data.data.refreshToken);

            // Retry original request
            originalRequest.headers['Authorization'] = `Bearer ${refreshTokenResponse.data.data.accessToken}`;
            return apiClient(originalRequest);
          }
        } catch (refreshError) {
          // If refresh fails, logout user
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          router.push({ name: 'login' });
          toast.error('Session expired. Please login again.');
        }
      } else {
        // No refresh token, redirect to login
        router.push({ name: 'login' });
        toast.error('Please login to continue.');
      }
    }

    // Get error message from response if available
    const errorMessage = error.response?.data?.errorMessage || error.message;
    return Promise.reject({ ...error, message: errorMessage });
  }
);

function getOrCreateDeviceId(): string {
  let deviceId = localStorage.getItem(DEVICE_ID_HEADER);
  if (!deviceId) {
    deviceId = generateDeviceId();
    localStorage.setItem(DEVICE_ID_HEADER, deviceId);
  }
  return deviceId;
}

export default apiClient;

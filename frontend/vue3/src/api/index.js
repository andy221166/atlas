// axios/index.js
import axios from 'axios';
import router from '@/router'; // Import router to handle redirection

// API Constants
export const API_BASE_URL = process.env.VUE_APP_API_BASE_URL;
export const NOTIFICATION_BASE_URL = process.env.VUE_APP_NOTIFICATION_BASE_URL;

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/api/auth/login',
    LOGOUT: '/api/auth/logout'
  },
  PRODUCTS: {
    LIST: '/api/front/products',
    SEARCH: '/api/front/products',
    DETAIL: (id) => `/api/front/products/${id}`,
    BRANDS: '/api/master/brands',
    CATEGORIES: '/api/master/categories'
  },
  ORDERS: {
    CREATE: '/api/front/orders/place',
    LIST: '/api/front/orders',
    STATUS: (id) => `/api/front/orders/${id}/status`
  },
  USERS: {
    REGISTER: '/api/front/users/register',
    PROFILE: '/api/front/users/profile'
  },
  NOTIFICATIONS: {
    WS: '/notification/ws',
    SSE: (orderId) => `/notification/sse/orders/${orderId}/status`
  }
};

export const API_TIMEOUT = 10000;

export const API_HEADERS = {
  'Content-Type': 'application/json'
};

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: API_TIMEOUT,
  headers: API_HEADERS,
  paramsSerializer: {
    indexes: null // This will serialize arrays without brackets
  }
});

// Request interceptor for adding auth token
apiClient.interceptors.request.use(
  config => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// Response interceptor for handling responses and errors
apiClient.interceptors.response.use(
  response => response,
  error => {
    const { response } = error;

    // Handle 401 Unauthorized errors
    if (response?.status === 401) {
      localStorage.removeItem('accessToken');
      router.push('/login');
      return Promise.reject(new Error('Unauthorized access'));
    }

    // Handle 403 Forbidden errors
    if (response?.status === 403) {
      return Promise.reject(new Error('Access forbidden'));
    }

    // Handle 404 Not Found errors
    if (response?.status === 404) {
      return Promise.reject(new Error('Resource not found'));
    }

    // Handle 500 Server errors
    if (response?.status === 500) {
      return Promise.reject(new Error('Internal server error'));
    }

    // Handle network errors
    if (!response) {
      return Promise.reject(new Error('Network error'));
    }

    return Promise.reject(error);
  }
);

// API methods
export const api = {
  // Auth methods
  auth: {
    login: (username, password) => 
      apiClient.post(API_ENDPOINTS.AUTH.LOGIN, { username, password }),
    logout: () => 
      apiClient.post(API_ENDPOINTS.AUTH.LOGOUT)
  },

  // Product methods
  products: {
    list: (params) => 
      apiClient.get(API_ENDPOINTS.PRODUCTS.LIST, { params }),
    search: (params) => 
      apiClient.post(API_ENDPOINTS.PRODUCTS.SEARCH, params),
    getDetail: (id) => 
      apiClient.get(API_ENDPOINTS.PRODUCTS.DETAIL(id)),
    listBrands: () => 
      apiClient.get(API_ENDPOINTS.PRODUCTS.BRANDS),
    listCategories: () => 
      apiClient.get(API_ENDPOINTS.PRODUCTS.CATEGORIES)
  },

  // Order methods
  orders: {
    create: (orderData) => 
      apiClient.post(API_ENDPOINTS.ORDERS.CREATE, orderData),
    list: (params) => 
      apiClient.get(API_ENDPOINTS.ORDERS.LIST, { params }),
    getDetail: (id) => 
      apiClient.get(API_ENDPOINTS.ORDERS.DETAIL(id)),
    getStatus: (id) => 
      apiClient.get(API_ENDPOINTS.ORDERS.STATUS(id))
  },

  // User methods
  users: {
    register: (userData) => 
      apiClient.post(API_ENDPOINTS.USERS.REGISTER, userData),
    getProfile: () => 
      apiClient.get(API_ENDPOINTS.USERS.PROFILE)
  },

  // Notification methods
  notifications: {
    getWebSocketUrl: () => 
      `${API_BASE_URL}${API_ENDPOINTS.NOTIFICATIONS.WS}`,
    getSSEUrl: (orderId) => 
      `${API_BASE_URL}${API_ENDPOINTS.NOTIFICATIONS.SSE(orderId)}`
  }
};

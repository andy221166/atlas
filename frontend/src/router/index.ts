import AuthLayout from '@/layouts/AuthLayout.vue';
import DefaultLayout from '@/layouts/DefaultLayout.vue';
import { useUserStore } from '@/stores/user.store';
import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    component: DefaultLayout,
    children: [
      {
        path: '',
        name: 'storeFront',
        component: () => import('@/pages/front/StoreFront.vue'),
      },
      {
        path: '/order-history',
        name: 'orderHistory',
        component: () => import('@/pages/front/OrderHistory.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: '/admin/dashboard',
        name: 'adminDashboard',
        component: () => import('@/pages/admin/Dashboard.vue'),
      },
      {
        path: '/admin/user',
        name: 'adminUserList',
        component: () => import('@/pages/admin/UserList.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product',
        name: 'adminProductList',
        component: () => import('@/pages/admin/ProductList.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/:id',
        name: 'adminProductInfo',
        component: () => import('@/pages/admin/ProductInfo.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/add',
        name: 'adminProductAdd',
        component: () => import('@/pages/admin/ProductAdd.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/:id/edit',
        name: 'adminProductEdit',
        component: () => import('@/pages/admin/ProductEdit.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/order',
        name: 'adminOrderList',
        component: () => import('@/pages/admin/OrderList.vue'),
        meta: { requiresAdmin: true }
      },
    ],
  },
  {
    path: '/auth',
    component: AuthLayout,
    children: [
      {
        path: '/login',
        name: 'login',
        component: () => import('@/pages/auth/Login.vue'),
      },
      {
        path: '/register',
        name: 'register',
        component: () => import('@/pages/auth/Register.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// Navigation guard
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  
  // Handle authentication/authorization
  try {
    // If we have a token but no profile, fetch it
    if (userStore.isAuthenticated && !userStore.profile) {
      await userStore.fetchProfile()
    }

    // Redirect unauthenticated users from protected routes
    if ((to.meta.requiresAuth || to.meta.requiresAdmin) && !userStore.isAuthenticated) {
      return next('/login')
    }
    
    // Redirect non-admin users from admin routes
    if (to.meta.requiresAdmin && userStore.profile?.role !== 'ADMIN') {
      return next('/')
    }
    
    // Redirect authenticated users from login
    if (to.path === '/login' && userStore.isAuthenticated) {
      return next('/')
    }
    
    // Auto-redirect admin to dashboard from home
    if (to.path === '/' && userStore.isAuthenticated && userStore.profile?.role === 'ADMIN') {
      return next({ name: 'adminDashboard' })
    }
    
    return next()
  } catch (error) {
    console.error('Navigation error:', error)
    return next('/')
  }
})

export default router

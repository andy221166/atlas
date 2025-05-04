import { useUserStore } from '@/stores/user.store';
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/Login.vue')
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('@/views/dashboard/Dashboard.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/users',
      name: 'users',
      component: () => import('@/views/user/UserList.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/products',
      name: 'products',
      component: () => import('@/views/product/ProductList.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/products/:id',
      name: 'product-info',
      component: () => import('@/views/product/ProductInfo.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/orders',
      name: 'orders',
      component: () => import('@/views/order/OrderManagement.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/',
      redirect: '/dashboard'
    }
  ]
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isAuthenticated) {
    next('/login')  // Redirect to login if trying to access protected page while not authenticated
  } else if (to.path === '/login' && userStore.isAuthenticated) {
    next('/dashboard')  // Redirect to dashboard if trying to access login while authenticated
  } else {
    next()
  }
});

export default router;

import { useUserStore } from '@/stores/user.store';
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/admin/dashboard',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/Login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/storefront/user/Register.vue')
    },
    {
      path: '/storefront',
      name: 'storefront',
      component: () => import('@/views/storefront/Storefront.vue')
    },
    {
      path: '/admin/dashboard',
      name: 'adminDashboard',
      component: () => import('@/views/admin/dashboard/Dashboard.vue'),
      meta: { requiresAdmin: true }
    },
    {
      path: '/admin/user',
      name: 'adminUserList',
      component: () => import('@/views/admin/user/UserList.vue'),
      meta: { requiresAdmin: true }
    },
    {
      path: '/admin/product',
      name: 'adminProductList',
      component: () => import('@/views/admin/product/ProductList.vue'),
      meta: { requiresAdmin: true }
    },
    {
      path: '/admin/product/:id',
      name: 'adminProductInfo',
      component: () => import('@/views/admin/product/ProductInfo.vue'),
      meta: { requiresAdmin: true }
    },
    {
      path: '/admin/product/add',
      name: 'adminProductAdd',
      component: () => import('@/views/admin/product/ProductAdd.vue'),
      meta: { requiresAdmin: true }
    },
    {
      path: '/admin/product/:id/edit',
      name: 'adminProductEdit',
      component: () => import('@/views/admin/product/ProductEdit.vue'),
      meta: { requiresAdmin: true }
    },
    {
      path: '/admin/order',
      name: 'adminOrderList',
      component: () => import('@/views/admin/order/OrderList.vue'),
      meta: { requiresAdmin: true }
    },
  ]
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if ((to.meta.requiresAuth || to.meta.requiresAdmin) && !userStore.isAuthenticated) {
    next('/login')  // Redirect to login if trying to access protected page while not authenticated
  } else if (to.path === '/login' && userStore.isAuthenticated) {
    next('/')  // Redirect to dashboard if trying to access login while authenticated
  } else {
    next()
  }
});

export default router;

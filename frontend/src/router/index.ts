import { useUserStore } from '@/stores/user.store'
import AuthLayout from '@/views/layouts/AuthLayout.vue';
import DefaultLayout from '@/views/layouts/DefaultLayout.vue';
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: DefaultLayout,
    children: [
      {
        path: '',
        name: 'storeFront',
        component: () => import('@/views/front/StoreFront.vue'),
      },
      {
        path: '/order-history',
        name: 'orderHistory',
        component: () => import('@/views/front/OrderHistory.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: '/admin/dashboard',
        name: 'adminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
      },
      {
        path: '/admin/user',
        name: 'adminUserList',
        component: () => import('@/views/admin/UserList.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product',
        name: 'adminProductList',
        component: () => import('@/views/admin/ProductList.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/:id',
        name: 'adminProductInfo',
        component: () => import('@/views/admin/ProductInfo.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/add',
        name: 'adminProductAdd',
        component: () => import('@/views/admin/ProductAdd.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/:id/edit',
        name: 'adminProductEdit',
        component: () => import('@/views/admin/ProductEdit.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/order',
        name: 'adminOrderList',
        component: () => import('@/views/admin/OrderList.vue'),
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
        component: () => import('@/views/auth/Login.vue'),
      },
      {
        path: '/register',
        name: 'register',
        component: () => import('@/views/auth/Register.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
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

export default router

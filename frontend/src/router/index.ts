import AdminLayout from '@/components/layouts/AdminLayout.vue'
import AuthLayout from '@/components/layouts/AuthLayout.vue'
import StoreFrontLayout from '@/components/layouts/StoreFrontLayout.vue'
import { useUserStore } from '@/stores/user.store'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: StoreFrontLayout,
    children: [
      {
        path: '',
        name: 'storeFront',
        component: () => import('@/pages/front/StoreFront.vue'),
      },
      {
        path: '/order-history',
        name: 'orderHistory',
        component: () => import('@/features/order/components/front/OrderHistory.vue'),
        meta: { requiresAuth: true }
      },
    ],
  },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      {
        path: '/admin/dashboard',
        name: 'adminDashboard',
        component: () => import('@/pages/admin/Dashboard.vue'),
      },
      {
        path: '/admin/user',
        name: 'adminUserList',
        component: () => import('@/features/user/components/admin/UserList.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product',
        name: 'adminProductList',
        component: () => import('@/features/product/components/admin/ProductList.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/:id',
        name: 'adminProductInfo',
        component: () => import('@/features/product/components/admin/ProductInfo.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/add',
        name: 'adminProductAdd',
        component: () => import('@/features/product/components/admin/ProductAdd.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/product/:id/edit',
        name: 'adminProductEdit',
        component: () => import('@/features/product/components/admin/ProductEdit.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: '/admin/order',
        name: 'adminOrderList',
        component: () => import('@/features/order/components/admin/OrderList.vue'),
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
        component: () => import('@/features/auth/components/Login.vue'),
      },
      {
        path: '/register',
        name: 'register',
        component: () => import('@/features/auth/components/Register.vue'),
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

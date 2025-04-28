import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import MainLayout from '../components/layout/MainLayout.vue'
import LoginPage from '../views/auth/LoginPage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      component: LoginPage,
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: MainLayout,
      meta: { requiresAuth: true },
      children: [
        {
          path: 'user',
          component: () => import('../views/user/UserList.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'product',
          component: () => import('../views/product/ProductList.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'product/create',
          component: () => import('../views/product/ProductCreate.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'product/:id/edit',
          component: () => import('../views/product/ProductEdit.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'product/:id',
          component: () => import('../views/product/ProductDetail.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'order',
          component: () => import('../views/order/OrderList.vue'),
          meta: { requiresAuth: true }
        }
      ]
    }
  ]
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.path === '/login' && authStore.isAuthenticated) {
    next('/')
  } else {
    next()
  }
})

export default router

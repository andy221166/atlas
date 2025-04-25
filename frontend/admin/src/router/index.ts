import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
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
      children: [
        {
          path: 'user',
          component: () => import('../views/user/UserList.vue')
        },
        {
          path: 'product',
          component: () => import('../views/product/ProductList.vue')
        },
        {
          path: 'product/create',
          component: () => import('../views/product/ProductCreate.vue')
        },
        {
          path: 'product/:id/edit',
          component: () => import('../views/product/ProductEdit.vue')
        },
        {
          path: 'product/:id',
          component: () => import('../views/product/ProductDetail.vue')
        },
        {
          path: 'order',
          component: () => import('../views/order/OrderList.vue')
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

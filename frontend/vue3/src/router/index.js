// router/index.js
import {createRouter, createWebHistory} from "vue-router";

// Lazy load components for better performance
const Storefront = () => import("@/pages/StorefrontPage.vue");
const OrderHistoryPage = () => import("@/pages/OrderHistoryPage.vue");
const RegisterPage = () => import("@/pages/RegisterPage.vue");
const LoginPage = () => import("@/pages/LoginPage.vue");

// Navigation guard helper
const checkAuth = (to, from, next) => {
  const accessToken = localStorage.getItem("accessToken");
  const isAuthenticated = accessToken && accessToken !== "undefined";

  if (to.meta.requiresAuth && !isAuthenticated) {
    return next({ 
      path: "/login",
      query: { redirect: to.fullPath }
    });
  }

  if (to.path === "/login" && isAuthenticated) {
    return next("/");
  }

  next();
};

const routes = [
  {
    path: "/",
    name: "Storefront",
    component: Storefront,
    meta: { requiresAuth: true }
  },
  {
    path: "/login",
    name: "Login",
    component: LoginPage,
    meta: { requiresAuth: false }
  },
  {
    path: "/register",
    name: "Register",
    component: RegisterPage,
    meta: { requiresAuth: false }
  },
  {
    path: "/order-history",
    name: "OrderHistory",
    component: OrderHistoryPage,
    meta: { requiresAuth: true }
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/"
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  }
});

// Global navigation guard
router.beforeEach(checkAuth);

export default router;

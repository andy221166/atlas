// router/index.js
import {createRouter, createWebHistory} from "vue-router";
import Storefront from "@/pages/StorefrontPage.vue";
import OrderHistoryPage from "@/pages/OrderHistoryPage.vue";
import RegisterPage from "@/pages/RegisterPage.vue";
import LoginPage from "@/pages/LoginPage.vue";

const routes = [
  {
    path: "/",
    redirect: () => {
      const accessToken = localStorage.getItem("accessToken");
      return accessToken ? "/storefront" : "/login";
    },
  },
  {
    path: "/login",
    component: LoginPage,
  },
  {
    path: "/register",
    component: RegisterPage,
  },
  {
    path: "/storefront",
    component: Storefront,
    meta: { requiresAuth: true },
  },
  {
    path: "/order-history",
    name: "OrderHistory",
    component: OrderHistoryPage,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const accessToken = localStorage.getItem("accessToken");

  if (to.matched.some((record) => record.meta.requiresAuth)) {
    // Redirect to login if the route requires auth and token is missing or invalid
    if (!accessToken || accessToken === "undefined") {
      return next("/login");
    }
  }

  // If user is logged in and tries to access the login page, redirect to storefront
  if (to.path === "/login" && accessToken) {
    return next("/storefront");
  }

  next(); // Allow navigation for other cases
});

export default router;

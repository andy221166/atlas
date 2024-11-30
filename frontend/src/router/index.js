// router/index.js
import {createRouter, createWebHistory} from "vue-router";
import Storefront from "@/pages/StorefrontPage.vue";
import OrderHistoryPage from "@/pages/OrderHistoryPage.vue";
import SignUpPage from "@/pages/SignUpPage.vue";
import SignInPage from "@/pages/SignInPage.vue";

const routes = [
  {
    path: "/",
    redirect: () => {
      const accessToken = localStorage.getItem("accessToken");
      return accessToken ? "/storefront" : "/sign-in";
    },
  },
  {
    path: "/sign-in",
    component: SignInPage,
  },
  {
    path: "/sign-up",
    component: SignUpPage,
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
    // Redirect to sign-in if the route requires auth and token is missing or invalid
    if (!accessToken || accessToken === "undefined") {
      return next("/sign-in");
    }
  }

  // If user is logged in and tries to access the sign-in page, redirect to storefront
  if (to.path === "/sign-in" && accessToken) {
    return next("/storefront");
  }

  next(); // Allow navigation for other cases
});

export default router;

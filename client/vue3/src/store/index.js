import {placeOrderApi} from "@/api/order";
import {getProfileApi} from "@/api/user";
import {createStore} from "vuex";

const store = createStore({
  state: {
    profile: null,
    cart: [],
    orderTracking: {
      orderId: null,
      status: "PROCESSING",
      canceledReason: null,
    },
  },
  mutations: {
    setProfile(state, profile) {
      state.profile = profile;
    },
    addToCart(state, product) {
      const existingItem = state.cart.find((item) => item.product.id === product.id);
      if (existingItem) {
        existingItem.quantity += product.quantity;
      } else {
        if (!state.cart.length) {
          state.orderTracking = { orderId: null, status: "PROCESSING", canceledReason: null };
        }
        state.cart.push({ product, quantity: product.quantity || 1 });
      }
    },
    removeFromCart(state, productId) {
      state.cart = state.cart.filter((item) => item.product.id !== productId);
    },
    clearCart(state) {
      state.cart = [];
    },
    setOrderTrackingInfo(state, trackingInfo) {
      state.orderTracking = { ...trackingInfo };
    },
  },
  actions: {
    async fetchProfile({ commit }) {
      try {
        const { data } = await getProfileApi();
        if (data.success) commit("setProfile", data.data);
      } catch (error) {
        console.error("Failed to fetch profile:", error);
      }
    },
    async placeOrder({ commit, state }) {
      try {
        const orderItems = state.cart.map((item) => ({
          productId: item.product.id,
          quantity: item.quantity,
        }));
        const response = await placeOrderApi({ orderItems });
        if (response.data.success) {
          commit("setOrderTrackingInfo", { orderId: response.data.data, status: "PROCESSING" });
          commit("clearCart");
        }
        return response.data;
      } catch (error) {
        console.error("Failed to place order:", error);
        throw error;
      }
    },
  },
  getters: {
    cartTotal(state) {
      return state.cart.reduce((total, item) => total + item.product.price * item.quantity, 0);
    },
  },
});

export default store;

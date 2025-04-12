import {api} from "@/api";
import {toast} from "vue3-toastify";

export default {
  namespaced: true,
  state: {
    profile: null,
  },
  mutations: {
    setProfile(state, profile) {
      state.profile = profile;
    },
  },
  actions: {
    async fetchProfile({ commit }) {
      try {
        const { data } = await api.users.getProfile();
        if (data.success) {
          commit("setProfile", data.data);
        } else {
          toast.error(data.message);
        }
      } catch (error) {
        toast.error("Failed to fetch profile: " + error.message);
      }
    },
  },
  getters: {
    profile: (state) => state.profile,
    isAuthenticated: (state) => !!state.profile,
  },
}; 
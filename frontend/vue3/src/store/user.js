import { api } from "@/api";

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
        if (data.success) commit("setProfile", data.data);
      } catch (error) {
        console.error("Failed to fetch profile:", error);
      }
    },
  },
  getters: {
    profile: (state) => state.profile,
    isAuthenticated: (state) => !!state.profile,
  },
}; 
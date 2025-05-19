import type { User } from '@/types/user.interface';
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    authenticated: false,
    profile: null as User | null,
    accessToken: localStorage.getItem('accessToken'),
    refreshToken: localStorage.getItem('refreshToken')
  }),
  getters: {
    // Now this getter name won't conflict with state
    isAuthenticated(): boolean {
      return this.accessToken !== null;
    },
    userRole: (state) => state.profile?.role,
    fullName: (state) => state.profile ? `${state.profile.firstName} ${state.profile.lastName}` : ''
  },
  actions: {
    setTokens(accessToken: string, refreshToken: string) {
      this.accessToken = accessToken;
      this.refreshToken = refreshToken;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
    },
    clearTokens() {
      this.accessToken = null;
      this.refreshToken = null;
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    },
    getAccessToken() {
      return this.accessToken;
    },
    getRefreshToken() {
      return this.refreshToken;
    },
    setProfile(profile: User) {
      this.profile = profile;
    },
    clearAuth() {
      this.clearTokens();
      this.profile = null;
    }
  }
});

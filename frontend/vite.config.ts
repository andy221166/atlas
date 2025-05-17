import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  define: {
    global: {}, // polyfill to satisfy some libraries
  },
  server: {
    port: 9000, // or any port you prefer
    open: true, // open browser on server start
  },
  resolve: {
    alias: {
      '@': '/src', // allows import like '@/components/Example.vue'
    },
  },
});

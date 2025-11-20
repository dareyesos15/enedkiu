import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Configura Vite para que sepa dónde buscar las APIs
    // Cuando el frontend pida /api/health, lo redirigirá a http://localhost:80/api/health
    // (que es Nginx)
    proxy: {
      '/api': {
        target: 'http://nginx', 
        changeOrigin: true,
        // Rewrites are not strictly necessary here, but good practice if you had a different internal structure
        // rewrite: (path) => path.replace(/^\/api/, '/api'),
      },
    },
    // Puerto interno del contenedor (aunque no lo usaremos en producción, es útil para el dev server)
    port: 5173, 
  }
});
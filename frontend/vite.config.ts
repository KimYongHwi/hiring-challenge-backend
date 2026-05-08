import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // 개발 시 브라우저는 동일 출처로 `/api`만 호출하고, Vite가 백엔드(8080)로 넘깁니다.
    proxy: {
      '/api': {
        // localhost 가 ::1 로만 해석되는 환경에서, IPv4 로만 뜬 Tomcat 과 엇갈리지 않게 127.0.0.1 사용
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
      },
    },
  },
})

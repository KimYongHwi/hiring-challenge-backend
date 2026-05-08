import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import './index.css'
import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    {/* 라우트 이동이 가능하도록 앱 루트를 BrowserRouter로 감쌉니다. */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </StrictMode>,
)

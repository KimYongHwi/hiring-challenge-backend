import { Navigate, NavLink, Route, Routes, useLocation } from 'react-router-dom'
import GoodsAddPage from './pages/GoodsAddPage'
import GoodsDetailPage from './pages/GoodsDetailPage'
import GoodsEditPage from './pages/GoodsEditPage'
import GoodsPage from './pages/GoodsPage'
import OrdersAddPage from './pages/OrdersAddPage'
import OrdersDetailPage from './pages/OrdersDetailPage'
import OrdersEditPage from './pages/OrdersEditPage'
import OrdersPage from './pages/OrdersPage'
import './App.css'

const App = () => {
  const location = useLocation()

  // 추후 라우팅/권한/활성화 상태를 연결하기 쉽도록 메뉴 메타데이터를 배열로 분리합니다.
  const lnbMenuList = [
    { label: '품목', to: '/goods' },
    { label: '주문서', to: '/orders' },
  ]

  const isLnbActive = (to: string) => {
    if (to === '/goods') {
      return location.pathname === '/goods' || location.pathname.startsWith('/goods/')
    }
    if (to === '/orders') {
      return location.pathname === '/orders' || location.pathname.startsWith('/orders/')
    }
    return location.pathname === to
  }

  return (
    <div className="layout">
      {/* 좌측 LNB 영역: 실제 라우트 링크를 제공하는 내비게이션 */}
      <aside className="lnb" aria-label="사이드 메뉴">
        <h1 className="lnbTitle">관리 메뉴</h1>
        <nav>
          <ul className="lnbMenuList">
            {lnbMenuList.map((menu) => (
              <li key={menu.to}>
                <NavLink
                  to={menu.to}
                  className={() =>
                    `lnbMenuButton ${isLnbActive(menu.to) ? 'lnbMenuButtonActive' : ''}`
                  }
                >
                  {menu.label}
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>
      </aside>

      {/* 우측 메인 영역: path 변경에 따라 뷰 컴포넌트를 조건 렌더링 */}
      <main className="content">
        <Routes>
          {/* 초기 진입 경로는 요구사항의 첫 메뉴인 /goods 로 통일합니다. */}
          <Route path="/" element={<Navigate to="/goods" replace />} />
          {/* 정적 경로를 동적 :id 보다 먼저 두어 add 가 id 로 오인되지 않게 합니다. */}
          <Route path="/goods/add" element={<GoodsAddPage />} />
          <Route path="/goods/:id/edit" element={<GoodsEditPage />} />
          <Route path="/goods/:id" element={<GoodsDetailPage />} />
          <Route path="/goods" element={<GoodsPage />} />
          <Route path="/orders/add" element={<OrdersAddPage />} />
          <Route path="/orders/:id/edit" element={<OrdersEditPage />} />
          <Route path="/orders/:id" element={<OrdersDetailPage />} />
          <Route path="/orders" element={<OrdersPage />} />
          <Route path="*" element={<Navigate to="/goods" replace />} />
        </Routes>
      </main>
    </div>
  )
}

export default App

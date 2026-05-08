import { useCallback, useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { deleteOrder, fetchOrdersPage } from '../api/orders'
import type { OrderListItem, SpringPage } from '../api/types'

const OrdersPage = () => {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [pageData, setPageData] = useState<SpringPage<OrderListItem> | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [deletingId, setDeletingId] = useState<string | null>(null)

  const loadPage = useCallback(async (p: number) => {
    setLoading(true)
    setError(null)
    try {
      const data = await fetchOrdersPage(p, 10)
      setPageData(data)
    } catch (e) {
      setError(e instanceof Error ? e.message : '목록을 불러오지 못했습니다.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    const taskId = window.setTimeout(() => {
      void loadPage(page)
    }, 0)
    return () => window.clearTimeout(taskId)
  }, [loadPage, page])

  const handleDelete = async (orderId: string) => {
    if (!window.confirm('이 주문을 삭제할까요? 연결된 재고 복구는 서버에서 처리합니다.')) {
      return
    }
    setDeletingId(orderId)
    try {
      await deleteOrder(orderId)
      await loadPage(page)
    } catch (e) {
      window.alert(e instanceof Error ? e.message : '삭제에 실패했습니다.')
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <div className="pageStack">
      <section className="contentPanel">
        <div className="panelHeader">
          <div>
            <h2>주문 조회</h2>
            <p className="pageLead">
              목록은 주문 품목 수와 총액만 표시합니다. 행을 클릭하면 줄 단가 스냅샷이 포함된 상세 화면으로 이동합니다.
            </p>
          </div>
          <Link to="/orders/add" className="btnPrimary linkButton">
            + 추가
          </Link>
        </div>

        {error ? <p className="formError">{error}</p> : null}
        {loading ? <p className="muted">불러오는 중…</p> : null}

        <div className="tableWrap">
          <table className="dataTable dataTableInteractive">
            <colgroup>
              <col className="colListId" />
              <col className="colListOrderer" />
              <col className="colListGrow" />
              <col className="colListPhone" />
              <col className="colListCount" />
              <col className="colListNum" />
              <col className="colListActions" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col" title="주문 ID">
                  ID
                </th>
                <th scope="col">주문자</th>
                <th scope="col" className="cellGrow">
                  주소
                </th>
                <th scope="col">전화</th>
                <th scope="col" className="cellNum">
                  품목 수
                </th>
                <th scope="col" className="cellNum">
                  총액(원)
                </th>
                <th scope="col" className="cellActions">
                  작업
                </th>
              </tr>
            </thead>
            <tbody>
              {(pageData?.content ?? []).map((row) => (
                <tr
                  key={row.orderId}
                  className="dataTableClickRow"
                  tabIndex={0}
                  role="button"
                  aria-label={`주문 상세: ${row.orderId}`}
                  onClick={() => navigate(`/orders/${row.orderId}`)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter' || e.key === ' ') {
                      e.preventDefault()
                      navigate(`/orders/${row.orderId}`)
                    }
                  }}
                >
                  <td className="cellMono" title={row.orderId}>
                    {row.orderId}
                  </td>
                  <td>{row.ordererName}</td>
                  <td className="cellGrow">{row.address}</td>
                  <td>{row.phoneNo}</td>
                  <td className="cellNum">{row.lineItemCount.toLocaleString()}</td>
                  <td className="cellNum">{row.totalAmount.toLocaleString()}</td>
                  <td className="cellActions" onClick={(e) => e.stopPropagation()}>
                    {/* flex는 td가 아닌 내부 div에만 (테이블 열 정렬 유지) */}
                    <div className="rowActions">
                      <Link to={`/orders/${row.orderId}/edit`} className="btnInline btnSecondary linkButton">
                        수정
                      </Link>
                      <button
                        type="button"
                        className="btnInline btnGhost"
                        disabled={deletingId === row.orderId}
                        onClick={() => void handleDelete(row.orderId)}
                      >
                        삭제
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="pager">
          <button
            type="button"
            className="btnSecondary"
            disabled={loading || !pageData || pageData.first}
            onClick={() => setPage((p) => Math.max(0, p - 1))}
          >
            이전
          </button>
          <span className="pagerInfo">
            {pageData
              ? `${pageData.number + 1} / ${Math.max(1, pageData.totalPages)} 페이지 · 총 ${pageData.totalElements}건`
              : '—'}
          </span>
          <button
            type="button"
            className="btnSecondary"
            disabled={loading || !pageData || pageData.last}
            onClick={() => setPage((p) => p + 1)}
          >
            다음
          </button>
        </div>
      </section>
    </div>
  )
}

export default OrdersPage

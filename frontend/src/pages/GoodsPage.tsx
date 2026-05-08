import { useCallback, useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { deleteProduct, fetchProductsPage } from '../api/products'
import type { Product, SpringPage } from '../api/types'

const GoodsPage = () => {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [pageData, setPageData] = useState<SpringPage<Product> | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [deletingId, setDeletingId] = useState<string | null>(null)

  const loadPage = useCallback(async (p: number) => {
    setLoading(true)
    setError(null)
    try {
      const data = await fetchProductsPage(p, 10)
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

  const handleDelete = async (productId: string) => {
    if (!window.confirm('이 품목을 삭제할까요? 주문에 포함된 품목은 삭제되지 않을 수 있습니다.')) {
      return
    }
    setDeletingId(productId)
    try {
      await deleteProduct(productId)
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
            <h2>품목 조회</h2>
            <p className="pageLead">
              품목 목록은 서버 페이지네이션(한 페이지 최대 10건)과 동일합니다. 행을 클릭하면 상세 화면으로 이동합니다.
            </p>
          </div>
          <Link to="/goods/add" className="btnPrimary linkButton">
            + 추가
          </Link>
        </div>

        {error ? <p className="formError">{error}</p> : null}
        {loading ? <p className="muted">불러오는 중…</p> : null}

        <div className="tableWrap">
          <table className="dataTable dataTableInteractive">
            <colgroup>
              <col className="colListId" />
              <col className="colListGrow" />
              <col className="colListUnit" />
              <col className="colListNum" />
              <col className="colListNum" />
              <col className="colListActions" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">ID</th>
                <th scope="col" className="cellGrow">
                  품목명
                </th>
                <th scope="col">단위</th>
                <th scope="col" className="cellNum">
                  단가(원)
                </th>
                <th scope="col" className="cellNum">
                  재고
                </th>
                <th scope="col" className="cellActions">
                  작업
                </th>
              </tr>
            </thead>
            <tbody>
              {(pageData?.content ?? []).map((row) => (
                <tr
                  key={row.productId}
                  className="dataTableClickRow"
                  tabIndex={0}
                  role="button"
                  aria-label={`품목 상세: ${row.productName}`}
                  onClick={() => navigate(`/goods/${row.productId}`)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter' || e.key === ' ') {
                      e.preventDefault()
                      navigate(`/goods/${row.productId}`)
                    }
                  }}
                >
                  <td className="cellMono" title={row.productId}>
                    {row.productId}
                  </td>
                  <td className="cellGrow">{row.productName}</td>
                  <td>{row.unit}</td>
                  <td className="cellNum">{row.unitPrice.toLocaleString()}</td>
                  <td className="cellNum">{row.stockQty.toLocaleString()}</td>
                  <td className="cellActions" onClick={(e) => e.stopPropagation()}>
                    {/* td에 display:flex를 두면 테이블 열 그리드가 깨져 헤더와 본문 열이 어긋날 수 있어, flex는 내부 div에만 둡니다. */}
                    <div className="rowActions">
                      <Link to={`/goods/${row.productId}/edit`} className="btnInline btnSecondary linkButton">
                        수정
                      </Link>
                      <button
                        type="button"
                        className="btnInline btnGhost"
                        disabled={deletingId === row.productId}
                        onClick={() => void handleDelete(row.productId)}
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

export default GoodsPage

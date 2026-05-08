import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { fetchProductById } from '../api/products'
import type { Product } from '../api/types'

const GoodsDetailPage = () => {
  const { id } = useParams<{ id: string }>()
  const [product, setProduct] = useState<Product | null>(null)
  const [loading, setLoading] = useState(Boolean(id))
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!id) {
      return
    }

    const taskId = window.setTimeout(() => {
      const run = async () => {
        setLoading(true)
        setError(null)
        try {
          const data = await fetchProductById(id)
          setProduct(data)
        } catch (e) {
          setError(e instanceof Error ? e.message : '불러오지 못했습니다.')
          setProduct(null)
        } finally {
          setLoading(false)
        }
      }
      void run()
    }, 0)

    return () => window.clearTimeout(taskId)
  }, [id])

  if (!id) {
    return (
      <div className="pageStack">
        <section className="contentPanel">
          <p className="formError">품목 ID가 없습니다.</p>
          <Link to="/goods" className="btnSecondary linkButton">
            목록으로
          </Link>
        </section>
      </div>
    )
  }

  return (
    <div className="pageStack">
      <section className="contentPanel">
        <div className="panelHeader">
          <div>
            <h2>품목 상세</h2>
            <p className="pageLead">목록에서 선택한 품목의 전체 필드를 표시합니다.</p>
          </div>
          <div className="panelHeaderActions">
            <Link to={`/goods/${id}/edit`} className="btnSecondary linkButton">
              수정
            </Link>
            <Link to="/goods" className="btnSecondary linkButton">
              목록으로
            </Link>
          </div>
        </div>

        {loading ? <p className="muted">불러오는 중…</p> : null}
        {error ? <p className="formError">{error}</p> : null}

        {product ? (
          <dl className="detailGrid">
            <dt>품목 ID</dt>
            <dd className="cellMono">{product.productId}</dd>
            <dt>품목명</dt>
            <dd>{product.productName}</dd>
            <dt>단위</dt>
            <dd>{product.unit}</dd>
            <dt>단가(원)</dt>
            <dd className="cellNum">{product.unitPrice.toLocaleString()}</dd>
            <dt>재고 수량</dt>
            <dd className="cellNum">{product.stockQty.toLocaleString()}</dd>
          </dl>
        ) : null}
      </section>
    </div>
  )
}

export default GoodsDetailPage

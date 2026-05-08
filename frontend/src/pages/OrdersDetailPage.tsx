import { useEffect, useMemo, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { fetchOrderById } from '../api/orders'
import type { OrderDetail } from '../api/types'

const OrdersDetailPage = () => {
  const { id } = useParams<{ id: string }>()
  const [order, setOrder] = useState<OrderDetail | null>(null)
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
          const data = await fetchOrderById(id)
          setOrder(data)
        } catch (e) {
          setError(e instanceof Error ? e.message : '불러오지 못했습니다.')
          setOrder(null)
        } finally {
          setLoading(false)
        }
      }
      void run()
    }, 0)

    return () => window.clearTimeout(taskId)
  }, [id])

  const lineTotal = useMemo(() => {
    if (!order) {
      return 0
    }
    return order.orderLines.reduce((sum, line) => sum + line.qty * line.unitPrice, 0)
  }, [order])

  if (!id) {
    return (
      <div className="pageStack">
        <section className="contentPanel">
          <p className="formError">주문 ID가 없습니다.</p>
          <Link to="/orders" className="btnSecondary linkButton">
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
            <h2>주문 상세</h2>
            <p className="pageLead">주문 헤더와 줄 단위 스냅샷 단가·수량을 함께 보여 줍니다.</p>
          </div>
          <div className="panelHeaderActions">
            <Link to={`/orders/${id}/edit`} className="btnSecondary linkButton">
              수정
            </Link>
            <Link to="/orders" className="btnSecondary linkButton">
              목록으로
            </Link>
          </div>
        </div>

        {loading ? <p className="muted">불러오는 중…</p> : null}
        {error ? <p className="formError">{error}</p> : null}

        {order ? (
          <>
            <dl className="detailGrid">
              <dt>주문 ID</dt>
              <dd className="cellMono">{order.orderId}</dd>
              <dt>주문자명</dt>
              <dd>{order.ordererName}</dd>
              <dt>주소</dt>
              <dd>{order.address}</dd>
              <dt>전화번호</dt>
              <dd>{order.phoneNo}</dd>
            </dl>

            <h3 className="detailSubheading">주문 품목</h3>
            <div className="tableWrap">
              <table className="dataTable">
                <thead>
                  <tr>
                    <th scope="col">품목 ID</th>
                    <th scope="col" className="cellNum">
                      수량
                    </th>
                    <th scope="col" className="cellNum">
                      단가(원)
                    </th>
                    <th scope="col" className="cellNum">
                      금액(원)
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {order.orderLines.map((line, index) => (
                    <tr key={`${index}-${line.productId}-${line.qty}-${line.unitPrice}`}>
                      <td className="cellMono">{line.productId}</td>
                      <td className="cellNum">{line.qty.toLocaleString()}</td>
                      <td className="cellNum">{line.unitPrice.toLocaleString()}</td>
                      <td className="cellNum">{(line.qty * line.unitPrice).toLocaleString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <p className="pageLead">
              줄 합계(검산): <strong>{lineTotal.toLocaleString()}</strong> 원
            </p>
          </>
        ) : null}
      </section>
    </div>
  )
}

export default OrdersDetailPage

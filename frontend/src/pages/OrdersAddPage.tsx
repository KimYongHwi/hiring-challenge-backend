import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { fetchAllProducts } from '../api/products'
import { createOrder } from '../api/orders'
import { formatOrderProductSelectLabel } from '../utils/orderProductSelectLabel'

type DraftLine = {
  key: string
  productId: string
  qty: string
}

/**
 * 주문 등록 전용 화면입니다.
 * 모달 대신 라우트로 분리했습니다.
 */
const OrdersAddPage = () => {
  const navigate = useNavigate()

  const [products, setProducts] = useState<
    { productId: string; productName: string; unit: string }[]
  >([])
  const [productsError, setProductsError] = useState<string | null>(null)

  const [ordererName, setOrdererName] = useState('')
  const [address, setAddress] = useState('')
  const [phoneNo, setPhoneNo] = useState('')
  const [lines, setLines] = useState<DraftLine[]>([
    { key: crypto.randomUUID(), productId: '', qty: '1' },
  ])
  const [submitting, setSubmitting] = useState(false)
  const [formMessage, setFormMessage] = useState<string | null>(null)

  useEffect(() => {
    const loadProducts = async () => {
      setProductsError(null)
      try {
        const all = await fetchAllProducts()
        setProducts(
          all.map((p) => ({
            productId: p.productId,
            productName: typeof p.productName === 'string' ? p.productName : '',
            unit: typeof p.unit === 'string' ? p.unit : '',
          })),
        )
      } catch (e) {
        setProductsError(e instanceof Error ? e.message : '품목 목록을 불러오지 못했습니다.')
      }
    }
    const taskId = window.setTimeout(() => {
      void loadProducts()
    }, 0)
    return () => window.clearTimeout(taskId)
  }, [])

  const addLine = () => {
    setLines((prev) => [...prev, { key: crypto.randomUUID(), productId: '', qty: '1' }])
  }

  const removeLine = (key: string) => {
    setLines((prev) => (prev.length <= 1 ? prev : prev.filter((l) => l.key !== key)))
  }

  const updateLine = (key: string, patch: Partial<Pick<DraftLine, 'productId' | 'qty'>>) => {
    setLines((prev) => prev.map((l) => (l.key === key ? { ...l, ...patch } : l)))
  }

  const handleSubmit = async () => {
    setFormMessage(null)

    if (!ordererName.trim() || !address.trim() || !phoneNo.trim()) {
      setFormMessage('주문자명, 주소, 전화번호는 필수입니다.')
      return
    }

    const parsedLines = lines
      .map((l) => ({
        productId: l.productId.trim(),
        qty: Number(l.qty),
      }))
      .filter((l) => l.productId.length > 0)

    if (parsedLines.length === 0) {
      setFormMessage('최소 1개의 주문 품목을 선택해 주세요.')
      return
    }

    if (parsedLines.some((l) => !Number.isInteger(l.qty) || l.qty < 1)) {
      setFormMessage('수량은 1 이상의 정수여야 합니다.')
      return
    }

    setSubmitting(true)
    try {
      await createOrder({
        ordererName: ordererName.trim(),
        address: address.trim(),
        phoneNo: phoneNo.trim(),
        orderLines: parsedLines.map((l) => ({ productId: l.productId, qty: l.qty })),
      })
      navigate('/orders')
    } catch (e) {
      setFormMessage(e instanceof Error ? e.message : '등록에 실패했습니다.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="pageStack">
      <section className="contentPanel">
        <div className="panelHeader">
          <div>
            <h2>주문 등록</h2>
            <p className="pageLead">주문 정보와 품목 줄을 입력한 뒤 등록합니다.</p>
          </div>
          <Link to="/orders" className="btnSecondary linkButton">
            목록으로
          </Link>
        </div>

        {productsError ? <p className="formError">{productsError}</p> : null}

        <div className="formGrid">
          <label className="field fieldSpan2">
            <span className="fieldLabel">주문자명</span>
            <input value={ordererName} onChange={(e) => setOrdererName(e.target.value)} />
          </label>
          <label className="field fieldSpan2">
            <span className="fieldLabel">주소</span>
            <input value={address} onChange={(e) => setAddress(e.target.value)} />
          </label>
          <label className="field fieldSpan2">
            <span className="fieldLabel">전화번호</span>
            <input value={phoneNo} onChange={(e) => setPhoneNo(e.target.value)} />
          </label>
        </div>

        <div className="linesHeader">
          <h4>주문 품목</h4>
          <button type="button" className="btnSecondary" onClick={addLine}>
            줄 추가
          </button>
        </div>

        <div className="lineList">
          {lines.map((line, index) => (
            <div key={line.key} className="lineRow">
              <span className="lineIndex">{index + 1}</span>
              <label className="field">
                <span className="fieldLabel">품목</span>
                <select
                  value={line.productId}
                  onChange={(e) => updateLine(line.key, { productId: e.target.value })}
                >
                  <option value="">선택</option>
                  {products.map((p) => (
                    <option key={p.productId} value={p.productId}>
                      {formatOrderProductSelectLabel(p)}
                    </option>
                  ))}
                </select>
              </label>
              <label className="field">
                <span className="fieldLabel">수량</span>
                <input
                  inputMode="numeric"
                  value={line.qty}
                  onChange={(e) => updateLine(line.key, { qty: e.target.value })}
                />
              </label>
              <button type="button" className="btnGhost" disabled={lines.length <= 1} onClick={() => removeLine(line.key)}>
                줄 제거
              </button>
            </div>
          ))}
        </div>

        <p className="muted">
          품목 선택 목록은 API 페이지 제한(10건)을 고려해 전체 품목을 순차 로딩합니다. (현재 {products.length}건)
        </p>

        {formMessage ? <p className="formError">{formMessage}</p> : null}

        <div className="formActions">
          <button type="button" className="btnPrimary" disabled={submitting} onClick={() => void handleSubmit()}>
            주문 등록
          </button>
        </div>
      </section>
    </div>
  )
}

export default OrdersAddPage

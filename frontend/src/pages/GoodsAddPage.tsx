import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { createProduct } from '../api/products'

/**
 * 품목 등록 전용 화면입니다.
 * 모달 대신 라우트로 분리해, 지원자가 원하면 다른 UX로 확장하기 쉽게 두었습니다.
 */
const GoodsAddPage = () => {
  const navigate = useNavigate()

  const [formName, setFormName] = useState('')
  const [formUnit, setFormUnit] = useState('')
  const [formPrice, setFormPrice] = useState('')
  const [formStock, setFormStock] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [formMessage, setFormMessage] = useState<string | null>(null)

  const handleSubmit = async () => {
    setFormMessage(null)
    const unitPrice = Number(formPrice)
    const stockQty = Number(formStock)
    if (!formName.trim() || !formUnit.trim()) {
      setFormMessage('품목명과 단위는 필수입니다.')
      return
    }
    if (!Number.isFinite(unitPrice) || unitPrice < 0) {
      setFormMessage('단가는 0 이상의 숫자로 입력해 주세요.')
      return
    }
    if (!Number.isInteger(stockQty) || stockQty < 0) {
      setFormMessage('재고 수량은 0 이상의 정수로 입력해 주세요.')
      return
    }

    setSubmitting(true)
    try {
      await createProduct({
        productName: formName.trim(),
        unit: formUnit.trim(),
        unitPrice,
        stockQty,
      })
      navigate('/goods')
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
            <h2>품목 등록</h2>
            <p className="pageLead">필수 필드를 입력한 뒤 등록합니다.</p>
          </div>
          <Link to="/goods" className="btnSecondary linkButton">
            목록으로
          </Link>
        </div>

        <div className="formGrid">
          <label className="field">
            <span className="fieldLabel">품목명</span>
            <input value={formName} onChange={(e) => setFormName(e.target.value)} />
          </label>
          <label className="field">
            <span className="fieldLabel">단위</span>
            <input value={formUnit} onChange={(e) => setFormUnit(e.target.value)} placeholder="예: 박스" />
          </label>
          <label className="field">
            <span className="fieldLabel">단가(원)</span>
            <input
              inputMode="decimal"
              value={formPrice}
              onChange={(e) => setFormPrice(e.target.value)}
              placeholder="0"
            />
          </label>
          <label className="field">
            <span className="fieldLabel">재고 수량</span>
            <input
              inputMode="numeric"
              value={formStock}
              onChange={(e) => setFormStock(e.target.value)}
              placeholder="0"
            />
          </label>
        </div>

        {formMessage ? <p className="formError">{formMessage}</p> : null}

        <div className="formActions">
          <button type="button" className="btnPrimary" disabled={submitting} onClick={() => void handleSubmit()}>
            등록
          </button>
        </div>
      </section>
    </div>
  )
}

export default GoodsAddPage

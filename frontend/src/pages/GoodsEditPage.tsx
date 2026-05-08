import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { fetchProductById, updateProduct } from '../api/products'

/**
 * 품목 수정: 등록 화면과 동일 필드에 기존 값을 채운 뒤 PUT 합니다.
 */
const GoodsEditPage = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()

  const [loadError, setLoadError] = useState<string | null>(null)
  // id가 없으면 로드 이펙트가 돌지 않으므로 초기 로딩은 false로 두고, id가 있을 때만 true로 시작합니다.
  const [initialLoading, setInitialLoading] = useState(() => Boolean(id))

  const [formName, setFormName] = useState('')
  const [formUnit, setFormUnit] = useState('')
  const [formPrice, setFormPrice] = useState('')
  const [formStock, setFormStock] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [formMessage, setFormMessage] = useState<string | null>(null)

  useEffect(() => {
    if (!id) {
      return
    }

    const taskId = window.setTimeout(() => {
      const run = async () => {
        setInitialLoading(true)
        setLoadError(null)
        try {
          const p = await fetchProductById(id)
          setFormName(p.productName)
          setFormUnit(p.unit)
          setFormPrice(String(p.unitPrice))
          setFormStock(String(p.stockQty))
        } catch (e) {
          setLoadError(e instanceof Error ? e.message : '불러오지 못했습니다.')
        } finally {
          setInitialLoading(false)
        }
      }
      void run()
    }, 0)

    return () => window.clearTimeout(taskId)
  }, [id])

  const handleSubmit = async () => {
    if (!id) {
      return
    }
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
      await updateProduct(id, {
        productName: formName.trim(),
        unit: formUnit.trim(),
        unitPrice,
        stockQty,
      })
      navigate('/goods')
    } catch (e) {
      setFormMessage(e instanceof Error ? e.message : '수정에 실패했습니다.')
    } finally {
      setSubmitting(false)
    }
  }

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
            <h2>품목 수정</h2>
            <p className="pageLead">등록 화면과 동일한 필드로 값을 수정한 뒤 저장합니다.</p>
          </div>
          <Link to="/goods" className="btnSecondary linkButton">
            목록으로
          </Link>
        </div>

        {initialLoading ? <p className="muted">불러오는 중…</p> : null}
        {loadError ? <p className="formError">{loadError}</p> : null}

        {!initialLoading && !loadError ? (
          <>
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
                저장
              </button>
            </div>
          </>
        ) : null}
      </section>
    </div>
  )
}

export default GoodsEditPage

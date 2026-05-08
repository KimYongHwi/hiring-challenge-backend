/**
 * 주문 추가·수정 화면의 품목 `<select>` 옵션에 쓰는 표시 문자열입니다.
 * 형식: 품목명 / 단위 — 예: 양파 / 망, 양파 / kg
 *
 * 런타임에 `unit` 이 빠지면 JS에서 `... + undefined` 가 `"undefined"` 문자열이 되므로,
 * 항상 문자열로 보정합니다.
 */
export type OrderProductSelectRow = {
  productName: string
  unit?: string | null
}

export const formatOrderProductSelectLabel = (row: OrderProductSelectRow): string => {
  const name = String(row.productName ?? '').trim()
  const unitPart = String(row.unit ?? '').trim()
  if (unitPart.length === 0) {
    return name.length > 0 ? name : '품목'
  }
  if (name.length === 0) {
    return unitPart
  }
  return name + ' / ' + unitPart
}

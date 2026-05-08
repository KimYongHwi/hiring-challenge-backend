/**
 * README 및 백엔드 DTO와 맞춘 공통 타입입니다.
 * 수정/삭제 API는 과제 범위 밖이므로 요청 타입은 등록·조회에 필요한 것만 둡니다.
 */

export type Product = {
  productId: string
  productName: string
  unit: string
  unitPrice: number
  stockQty: number
}

/** Spring Data `Page` JSON의 프론트에서 쓰는 최소 필드만 표현합니다. */
export type SpringPage<T> = {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export type OrderListItem = {
  orderId: string
  ordererName: string
  address: string
  phoneNo: string
  lineItemCount: number
  totalAmount: number
}

export type OrderLine = {
  productId: string
  qty: number
  unitPrice: number
}

export type OrderDetail = {
  orderId: string
  ordererName: string
  address: string
  phoneNo: string
  orderLines: OrderLine[]
}

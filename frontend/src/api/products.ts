import { apiRequest } from './client'
import type { Product, SpringPage } from './types'

export const fetchProductsPage = (page: number, size = 10) =>
  apiRequest<SpringPage<Product>>(`/products?page=${page}&size=${size}`)

export const fetchProductById = (productId: string) =>
  apiRequest<Product>(`/products/${encodeURIComponent(productId)}`)

export type CreateProductBody = {
  productName: string
  unit: string
  unitPrice: number
  stockQty: number
}

/** 백엔드 Update DTO 와 동일 필드입니다. */
export type UpdateProductBody = CreateProductBody

export const createProduct = (body: CreateProductBody) =>
  apiRequest<Product>('/products', {
    method: 'POST',
    body: JSON.stringify(body),
  })

export const updateProduct = (productId: string, body: UpdateProductBody) =>
  apiRequest<Product>(`/products/${encodeURIComponent(productId)}`, {
    method: 'PUT',
    body: JSON.stringify(body),
  })

export const deleteProduct = (productId: string) =>
  apiRequest<void>(`/products/${encodeURIComponent(productId)}`, {
    method: 'DELETE',
  })

/**
 * 주문 등록 폼의 품목 선택용으로, 페이지 크기(최대 10) 제한을 고려해 전 페이지를 순회합니다.
 */
export const fetchAllProducts = async (): Promise<Product[]> => {
  const items: Product[] = []
  let page = 0
  const size = 10

  while (true) {
    const data = await fetchProductsPage(page, size)
    items.push(...data.content)
    if (data.last || data.content.length === 0) {
      break
    }
    page += 1
  }

  return items
}

import { apiRequest } from './client'
import type { OrderDetail, OrderListItem, SpringPage } from './types'

export const fetchOrdersPage = (page: number, size = 10) =>
  apiRequest<SpringPage<OrderListItem>>(`/orders?page=${page}&size=${size}`)

export const fetchOrderById = (orderId: string) =>
  apiRequest<OrderDetail>(`/orders/${encodeURIComponent(orderId)}`)

export type CreateOrderLineBody = {
  productId: string
  qty: number
}

export type CreateOrderBody = {
  ordererName: string
  address: string
  phoneNo: string
  orderLines: CreateOrderLineBody[]
}

export type UpdateOrderBody = CreateOrderBody

export const createOrder = (body: CreateOrderBody) =>
  apiRequest<OrderDetail>('/orders', {
    method: 'POST',
    body: JSON.stringify(body),
  })

export const updateOrder = (orderId: string, body: UpdateOrderBody) =>
  apiRequest<OrderDetail>(`/orders/${encodeURIComponent(orderId)}`, {
    method: 'PUT',
    body: JSON.stringify(body),
  })

export const deleteOrder = (orderId: string) =>
  apiRequest<void>(`/orders/${encodeURIComponent(orderId)}`, {
    method: 'DELETE',
  })

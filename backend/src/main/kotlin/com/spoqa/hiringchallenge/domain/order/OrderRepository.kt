package com.spoqa.hiringchallenge.domain.order

import com.spoqa.hiringchallenge.domain.order.vo.OrderId

interface OrderRepository {
    fun save(order: Order): Order

    fun findById(orderId: OrderId): Order?

    fun findAll(page: Int, size: Int): OrderPage

    fun deleteById(orderId: OrderId)
}

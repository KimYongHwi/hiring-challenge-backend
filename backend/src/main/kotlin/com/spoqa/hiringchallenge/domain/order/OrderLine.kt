package com.spoqa.hiringchallenge.domain.order

import com.spoqa.hiringchallenge.domain.order.vo.OrderQuantity
import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import com.spoqa.hiringchallenge.domain.product.vo.UnitPrice

data class OrderLine(
    val productId: ProductId,
    val qty: OrderQuantity,
    val unitPrice: UnitPrice,
) {
    fun totalAmount(): Long = unitPrice.value * qty.value
}

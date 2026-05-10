package com.spoqa.hiringchallenge.domain.order.fixture

import com.spoqa.hiringchallenge.domain.order.Order
import com.spoqa.hiringchallenge.domain.order.OrderLine
import com.spoqa.hiringchallenge.domain.order.vo.OrderAddress
import com.spoqa.hiringchallenge.domain.order.vo.OrderId
import com.spoqa.hiringchallenge.domain.order.vo.OrderQuantity
import com.spoqa.hiringchallenge.domain.order.vo.OrdererName
import com.spoqa.hiringchallenge.domain.order.vo.PhoneNo
import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import com.spoqa.hiringchallenge.domain.product.vo.UnitPrice
import java.util.UUID

object OrderFixtures {
    fun order(
        orderId: OrderId = OrderId(UUID.randomUUID()),
        ordererName: OrdererName = OrdererName("홍길동"),
        address: OrderAddress = OrderAddress("서울시 중구"),
        phoneNo: PhoneNo = PhoneNo("010-1234-5678"),
        orderLines: List<OrderLine> = listOf(orderLine()),
    ): Order =
        Order(
            orderId = orderId,
            ordererName = ordererName,
            address = address,
            phoneNo = phoneNo,
            orderLines = orderLines,
        )

    fun orderLine(
        productId: ProductId = ProductId(UUID.randomUUID()),
        unitPrice: Long = 1_000,
        qty: Int = 1,
    ): OrderLine =
        OrderLine(
            productId = productId,
            unitPrice = UnitPrice(unitPrice),
            qty = OrderQuantity(qty),
        )
}

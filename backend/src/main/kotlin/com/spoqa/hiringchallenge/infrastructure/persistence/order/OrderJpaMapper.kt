package com.spoqa.hiringchallenge.infrastructure.persistence.order

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

fun Order.toJpaEntity(): OrderJpaEntity {
    val orderEntity = OrderJpaEntity(
        orderId = orderId.value,
        ordererName = ordererName.value,
        address = address.value,
        phoneNo = phoneNo.value,
    )

    orderEntity.orderLines.addAll(
        orderLines.map { it.toJpaEntity(orderEntity) },
    )

    return orderEntity
}

fun OrderLine.toJpaEntity(orderEntity: OrderJpaEntity): OrderLineJpaEntity =
    OrderLineJpaEntity(
        orderLineId = UUID.randomUUID(),
        order = orderEntity,
        productId = productId.value,
        qty = qty.value,
        unitPrice = unitPrice.value,
    )

fun OrderJpaEntity.toDomain(): Order =
    Order(
        orderId = OrderId(orderId),
        ordererName = OrdererName(ordererName),
        address = OrderAddress(address),
        phoneNo = PhoneNo(phoneNo),
        orderLines = orderLines.map { it.toDomain() },
    )

fun OrderLineJpaEntity.toDomain(): OrderLine =
    OrderLine(
        productId = ProductId(productId),
        qty = OrderQuantity(qty),
        unitPrice = UnitPrice(unitPrice),
    )

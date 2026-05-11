package com.spoqa.hiringchallenge.application.order.mapper

import com.spoqa.hiringchallenge.application.order.dto.CreateOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.CreateOrderLineCommand
import com.spoqa.hiringchallenge.application.order.dto.UpdateOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.UpdateOrderLineCommand
import com.spoqa.hiringchallenge.domain.order.Order
import com.spoqa.hiringchallenge.domain.order.OrderLine
import com.spoqa.hiringchallenge.domain.order.vo.OrderAddress
import com.spoqa.hiringchallenge.domain.order.vo.OrderId
import com.spoqa.hiringchallenge.domain.order.vo.OrderQuantity
import com.spoqa.hiringchallenge.domain.order.vo.OrdererName
import com.spoqa.hiringchallenge.domain.order.vo.PhoneNo
import com.spoqa.hiringchallenge.domain.product.Product
import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import java.util.UUID

fun CreateOrderCommand.toOrder(orderLines: List<OrderLine>): Order =
    Order(
        orderId = OrderId(UUID.randomUUID()),
        ordererName = OrdererName(ordererName),
        address = OrderAddress(address),
        phoneNo = PhoneNo(phoneNo),
        orderLines = orderLines,
    )

fun CreateOrderLineCommand.toProductId(): ProductId =
    ProductId(productId)

fun CreateOrderLineCommand.toOrderLine(product: Product): OrderLine =
    OrderLine(
        productId = product.productId,
        qty = OrderQuantity(qty),
        unitPrice = product.unitPrice,
    )

fun UpdateOrderCommand.toOrdererName(): OrdererName =
    OrdererName(ordererName)

fun UpdateOrderCommand.toOrderAddress(): OrderAddress =
    OrderAddress(address)

fun UpdateOrderCommand.toPhoneNo(): PhoneNo =
    PhoneNo(phoneNo)

fun UpdateOrderLineCommand.toProductId(): ProductId =
    ProductId(productId)

fun UpdateOrderLineCommand.toOrderLine(product: Product): OrderLine =
    OrderLine(
        productId = product.productId,
        qty = OrderQuantity(qty),
        unitPrice = product.unitPrice,
    )

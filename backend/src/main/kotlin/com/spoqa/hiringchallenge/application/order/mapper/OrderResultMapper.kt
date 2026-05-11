package com.spoqa.hiringchallenge.application.order.mapper

import com.spoqa.hiringchallenge.application.order.dto.OrderDetailResult
import com.spoqa.hiringchallenge.application.order.dto.OrderLineResult
import com.spoqa.hiringchallenge.application.order.dto.OrderListItemResult
import com.spoqa.hiringchallenge.domain.order.Order
import com.spoqa.hiringchallenge.domain.order.OrderLine

fun Order.toDetailResult(): OrderDetailResult =
    OrderDetailResult(
        orderId = orderId.value,
        ordererName = ordererName.value,
        address = address.value,
        phoneNo = phoneNo.value,
        orderLines = orderLines.map { it.toResult() },
    )

fun Order.toListItemResult(): OrderListItemResult =
    OrderListItemResult(
        orderId = orderId.value,
        ordererName = ordererName.value,
        address = address.value,
        phoneNo = phoneNo.value,
        lineItemCount = lineItemCount(),
        totalAmount = totalAmount(),
    )

fun OrderLine.toResult(): OrderLineResult =
    OrderLineResult(
        productId = productId.value,
        qty = qty.value,
        unitPrice = unitPrice.value,
    )

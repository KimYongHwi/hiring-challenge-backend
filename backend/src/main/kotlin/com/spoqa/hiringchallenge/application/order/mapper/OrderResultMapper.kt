package com.spoqa.hiringchallenge.application.order.mapper

import com.spoqa.hiringchallenge.application.order.dto.OrderDetailResult
import com.spoqa.hiringchallenge.application.order.dto.OrderLineResult
import com.spoqa.hiringchallenge.application.order.dto.OrderListItemResult
import com.spoqa.hiringchallenge.application.order.dto.PageResult
import com.spoqa.hiringchallenge.domain.order.Order
import com.spoqa.hiringchallenge.domain.order.OrderLine
import com.spoqa.hiringchallenge.domain.order.OrderPage

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

fun OrderPage.toPageResult(): PageResult<OrderListItemResult> =
    PageResult(
        content = content.map { it.toListItemResult() },
        totalElements = totalElements,
        totalPages = totalPages,
        number = number,
        size = size,
        first = first,
        last = last,
    )

package com.spoqa.hiringchallenge.interfaces.order.mapper

import com.spoqa.hiringchallenge.application.order.dto.OrderDetailResult
import com.spoqa.hiringchallenge.application.order.dto.OrderLineResult
import com.spoqa.hiringchallenge.application.order.dto.OrderListItemResult
import com.spoqa.hiringchallenge.application.order.dto.PageResult
import com.spoqa.hiringchallenge.interfaces.order.dto.OrderDetailResponse
import com.spoqa.hiringchallenge.interfaces.order.dto.OrderLineResponse
import com.spoqa.hiringchallenge.interfaces.order.dto.OrderListItemResponse

fun OrderDetailResult.toResponse(): OrderDetailResponse =
    OrderDetailResponse(
        orderId = orderId,
        ordererName = ordererName,
        address = address,
        phoneNo = phoneNo,
        orderLines = orderLines.map { it.toResponse() },
    )

fun OrderLineResult.toResponse(): OrderLineResponse =
    OrderLineResponse(
        productId = productId,
        qty = qty,
        unitPrice = unitPrice,
    )

fun OrderListItemResult.toResponse(): OrderListItemResponse =
    OrderListItemResponse(
        orderId = orderId,
        ordererName = ordererName,
        address = address,
        phoneNo = phoneNo,
        lineItemCount = lineItemCount,
        totalAmount = totalAmount,
    )

fun PageResult<OrderListItemResult>.toOrderListItemResponsePage(): PageResult<OrderListItemResponse> =
    PageResult(
        content = content.map { it.toResponse() },
        totalElements = totalElements,
        totalPages = totalPages,
        number = number,
        size = size,
        first = first,
        last = last,
    )

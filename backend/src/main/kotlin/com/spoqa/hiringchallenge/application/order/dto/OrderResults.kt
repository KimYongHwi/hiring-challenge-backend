package com.spoqa.hiringchallenge.application.order.dto

import java.util.UUID

data class OrderListItemResult(
    val orderId: UUID,
    val ordererName: String,
    val address: String,
    val phoneNo: String,
    val lineItemCount: Int,
    val totalAmount: Long,
)

data class OrderDetailResult(
    val orderId: UUID,
    val ordererName: String,
    val address: String,
    val phoneNo: String,
    val orderLines: List<OrderLineResult>,
)

data class OrderLineResult(
    val productId: UUID,
    val qty: Int,
    val unitPrice: Long,
)

data class PageResult<T>(
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
)

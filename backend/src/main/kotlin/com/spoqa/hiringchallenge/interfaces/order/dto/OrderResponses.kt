package com.spoqa.hiringchallenge.interfaces.order.dto

import java.util.UUID

data class OrderDetailResponse(
    val orderId: UUID,
    val ordererName: String,
    val address: String,
    val phoneNo: String,
    val orderLines: List<OrderLineResponse>,
)

data class OrderLineResponse(
    val productId: UUID,
    val qty: Int,
    val unitPrice: Long,
)

data class OrderListItemResponse(
    val orderId: UUID,
    val ordererName: String,
    val address: String,
    val phoneNo: String,
    val lineItemCount: Int,
    val totalAmount: Long,
)

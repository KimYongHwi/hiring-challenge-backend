package com.spoqa.hiringchallenge.interfaces.order.dto

import java.util.UUID

data class CreateOrderRequest(
    val ordererName: String,
    val address: String,
    val phoneNo: String,
    val orderLines: List<CreateOrderLineRequest>,
)

data class CreateOrderLineRequest(
    val productId: UUID,
    val qty: Int,
)

data class UpdateOrderRequest(
    val ordererName: String,
    val address: String,
    val phoneNo: String,
    val orderLines: List<UpdateOrderLineRequest>,
)

data class UpdateOrderLineRequest(
    val productId: UUID,
    val qty: Int,
)

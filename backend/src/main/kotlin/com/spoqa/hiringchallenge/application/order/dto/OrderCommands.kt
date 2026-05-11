package com.spoqa.hiringchallenge.application.order.dto

import java.util.UUID

data class CreateOrderCommand(
    val ordererName: String,
    val address: String,
    val phoneNo: String,
    val orderLines: List<CreateOrderLineCommand>,
)

data class CreateOrderLineCommand(
    val productId: UUID,
    val qty: Int,
)

data class UpdateOrderCommand(
    val orderId: UUID,
    val ordererName: String,
    val address: String,
    val phoneNo: String,
    val orderLines: List<UpdateOrderLineCommand>,
)

data class UpdateOrderLineCommand(
    val productId: UUID,
    val qty: Int,
)

data class DeleteOrderCommand(
    val orderId: UUID,
)

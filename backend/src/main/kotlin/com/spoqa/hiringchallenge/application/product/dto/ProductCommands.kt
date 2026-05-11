package com.spoqa.hiringchallenge.application.product.dto

import java.util.UUID

data class CreateProductCommand(
    val productName: String,
    val unit: String,
    val unitPrice: Long,
    val stockQty: Int,
)

data class UpdateProductCommand(
    val productId: UUID,
    val productName: String,
    val unit: String,
    val unitPrice: Long,
    val stockQty: Int,
)

data class DeleteProductCommand(
    val productId: UUID,
)

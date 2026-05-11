package com.spoqa.hiringchallenge.interfaces.product.dto

import java.util.UUID

data class ProductResponse(
    val productId: UUID,
    val productName: String,
    val unit: String,
    val unitPrice: Long,
    val stockQty: Int,
)

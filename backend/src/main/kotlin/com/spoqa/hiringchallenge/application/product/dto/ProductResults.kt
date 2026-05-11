package com.spoqa.hiringchallenge.application.product.dto

import java.util.UUID

data class ProductResult(
    val productId: UUID,
    val productName: String,
    val unit: String,
    val unitPrice: Long,
    val stockQty: Int,
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

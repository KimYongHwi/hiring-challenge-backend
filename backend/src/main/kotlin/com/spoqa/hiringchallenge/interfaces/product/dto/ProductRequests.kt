package com.spoqa.hiringchallenge.interfaces.product.dto

data class CreateProductRequest(
    val productName: String,
    val unit: String,
    val unitPrice: Long,
    val stockQty: Int,
)

data class UpdateProductRequest(
    val productName: String,
    val unit: String,
    val unitPrice: Long,
    val stockQty: Int,
)

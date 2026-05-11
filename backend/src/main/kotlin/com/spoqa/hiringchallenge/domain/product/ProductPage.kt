package com.spoqa.hiringchallenge.domain.product

data class ProductPage(
    val content: List<Product>,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
)

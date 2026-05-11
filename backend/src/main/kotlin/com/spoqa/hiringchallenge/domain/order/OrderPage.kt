package com.spoqa.hiringchallenge.domain.order

data class OrderPage(
    val content: List<Order>,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
)

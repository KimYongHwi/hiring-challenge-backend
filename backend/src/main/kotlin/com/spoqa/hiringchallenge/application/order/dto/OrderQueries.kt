package com.spoqa.hiringchallenge.application.order.dto

import java.util.UUID

data class FindOrderQuery(
    val orderId: UUID,
)

data class FindOrdersQuery(
    val page: Int,
    val size: Int,
)

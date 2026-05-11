package com.spoqa.hiringchallenge.application.product.dto

import java.util.UUID

data class FindProductQuery(
    val productId: UUID,
)

data class FindProductsQuery(
    val page: Int,
    val size: Int,
)

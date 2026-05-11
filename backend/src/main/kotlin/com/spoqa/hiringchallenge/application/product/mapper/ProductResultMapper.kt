package com.spoqa.hiringchallenge.application.product.mapper

import com.spoqa.hiringchallenge.application.product.dto.ProductResult
import com.spoqa.hiringchallenge.domain.product.Product

object ProductResultMapper {
    fun toResult(product: Product): ProductResult =
        ProductResult(
            productId = product.productId.value,
            productName = product.productName.value,
            unit = product.unit.value,
            unitPrice = product.unitPrice.value,
            stockQty = product.stockQty.value,
        )
}

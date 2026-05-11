package com.spoqa.hiringchallenge.application.product.mapper

import com.spoqa.hiringchallenge.application.product.dto.PageResult
import com.spoqa.hiringchallenge.application.product.dto.ProductResult
import com.spoqa.hiringchallenge.domain.product.Product
import com.spoqa.hiringchallenge.domain.product.ProductPage

object ProductResultMapper {
    fun toResult(product: Product): ProductResult =
        ProductResult(
            productId = product.productId.value,
            productName = product.productName.value,
            unit = product.unit.value,
            unitPrice = product.unitPrice.value,
            stockQty = product.stockQty.value,
        )

    fun toPageResult(productPage: ProductPage): PageResult<ProductResult> =
        PageResult(
            content = productPage.content.map { toResult(it) },
            totalElements = productPage.totalElements,
            totalPages = productPage.totalPages,
            number = productPage.number,
            size = productPage.size,
            first = productPage.first,
            last = productPage.last,
        )
}

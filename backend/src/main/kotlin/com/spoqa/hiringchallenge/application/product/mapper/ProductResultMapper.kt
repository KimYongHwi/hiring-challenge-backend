package com.spoqa.hiringchallenge.application.product.mapper

import com.spoqa.hiringchallenge.application.product.dto.PageResult
import com.spoqa.hiringchallenge.application.product.dto.ProductResult
import com.spoqa.hiringchallenge.domain.product.Product
import com.spoqa.hiringchallenge.domain.product.ProductPage

fun Product.toResult(): ProductResult =
    ProductResult(
        productId = productId.value,
        productName = productName.value,
        unit = unit.value,
        unitPrice = unitPrice.value,
        stockQty = stockQty.value,
    )

fun ProductPage.toPageResult(): PageResult<ProductResult> =
    PageResult(
        content = content.map { it.toResult() },
        totalElements = totalElements,
        totalPages = totalPages,
        number = number,
        size = size,
        first = first,
        last = last,
    )

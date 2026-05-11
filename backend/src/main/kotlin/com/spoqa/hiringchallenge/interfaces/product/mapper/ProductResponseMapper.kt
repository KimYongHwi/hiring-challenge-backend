package com.spoqa.hiringchallenge.interfaces.product.mapper

import com.spoqa.hiringchallenge.application.product.dto.PageResult
import com.spoqa.hiringchallenge.application.product.dto.ProductResult
import com.spoqa.hiringchallenge.interfaces.product.dto.ProductResponse

fun ProductResult.toResponse(): ProductResponse =
    ProductResponse(
        productId = productId,
        productName = productName,
        unit = unit,
        unitPrice = unitPrice,
        stockQty = stockQty,
    )

fun PageResult<ProductResult>.toProductResponsePage(): PageResult<ProductResponse> =
    PageResult(
        content = content.map { it.toResponse() },
        totalElements = totalElements,
        totalPages = totalPages,
        number = number,
        size = size,
        first = first,
        last = last,
    )

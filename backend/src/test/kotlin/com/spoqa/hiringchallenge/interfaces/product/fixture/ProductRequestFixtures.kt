package com.spoqa.hiringchallenge.interfaces.product.fixture

import com.spoqa.hiringchallenge.interfaces.product.dto.CreateProductRequest
import com.spoqa.hiringchallenge.interfaces.product.dto.UpdateProductRequest

object ProductRequestFixtures {
    fun createProductRequest(
        productName: String = "콜라",
        unit: String = "캔",
        unitPrice: Long = 1_500,
        stockQty: Int = 20,
    ): CreateProductRequest =
        CreateProductRequest(
            productName = productName,
            unit = unit,
            unitPrice = unitPrice,
            stockQty = stockQty,
        )

    fun updateProductRequest(
        productName: String = "콜라 대용량",
        unit: String = "박스",
        unitPrice: Long = 12_000,
        stockQty: Int = 5,
    ): UpdateProductRequest =
        UpdateProductRequest(
            productName = productName,
            unit = unit,
            unitPrice = unitPrice,
            stockQty = stockQty,
        )
}

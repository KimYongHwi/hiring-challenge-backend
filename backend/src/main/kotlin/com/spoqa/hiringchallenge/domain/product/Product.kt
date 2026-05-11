package com.spoqa.hiringchallenge.domain.product

import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import com.spoqa.hiringchallenge.domain.product.vo.ProductName
import com.spoqa.hiringchallenge.domain.product.vo.ProductUnit
import com.spoqa.hiringchallenge.domain.product.vo.StockQuantity
import com.spoqa.hiringchallenge.domain.product.vo.UnitPrice

data class Product(
    val productId: ProductId,
    val productName: ProductName,
    val unit: ProductUnit,
    val unitPrice: UnitPrice,
    val stockQty: StockQuantity,
) {
    fun update(
        productName: ProductName,
        unit: ProductUnit,
        unitPrice: UnitPrice,
        stockQty: StockQuantity,
    ): Product =
        copy(
            productName = productName,
            unit = unit,
            unitPrice = unitPrice,
            stockQty = stockQty,
        )

    fun decreaseStock(quantity: StockQuantity): Product {
        require(stockQty.value >= quantity.value) { "재고 수량이 부족합니다." }

        return copy(
            stockQty = StockQuantity(stockQty.value - quantity.value),
        )
    }

    fun increaseStock(quantity: StockQuantity): Product =
        copy(
            stockQty = StockQuantity(stockQty.value + quantity.value),
        )
}

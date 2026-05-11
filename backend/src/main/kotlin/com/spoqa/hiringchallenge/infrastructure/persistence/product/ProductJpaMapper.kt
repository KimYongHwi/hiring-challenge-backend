package com.spoqa.hiringchallenge.infrastructure.persistence.product

import com.spoqa.hiringchallenge.domain.product.Product
import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import com.spoqa.hiringchallenge.domain.product.vo.ProductName
import com.spoqa.hiringchallenge.domain.product.vo.ProductUnit
import com.spoqa.hiringchallenge.domain.product.vo.StockQuantity
import com.spoqa.hiringchallenge.domain.product.vo.UnitPrice

fun Product.toJpaEntity(): ProductJpaEntity =
    ProductJpaEntity(
        productId = productId.value,
        productName = productName.value,
        unit = unit.value,
        unitPrice = unitPrice.value,
        stockQty = stockQty.value,
    )

fun ProductJpaEntity.toDomain(): Product =
    Product(
        productId = ProductId(productId),
        productName = ProductName(productName),
        unit = ProductUnit(unit),
        unitPrice = UnitPrice(unitPrice),
        stockQty = StockQuantity(stockQty),
    )

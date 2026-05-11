package com.spoqa.hiringchallenge.application.product.mapper

import com.spoqa.hiringchallenge.application.product.dto.CreateProductCommand
import com.spoqa.hiringchallenge.application.product.dto.UpdateProductCommand
import com.spoqa.hiringchallenge.domain.product.Product
import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import com.spoqa.hiringchallenge.domain.product.vo.ProductName
import com.spoqa.hiringchallenge.domain.product.vo.ProductUnit
import com.spoqa.hiringchallenge.domain.product.vo.StockQuantity
import com.spoqa.hiringchallenge.domain.product.vo.UnitPrice
import java.util.UUID

fun CreateProductCommand.toProduct(): Product =
    Product(
        productId = ProductId(UUID.randomUUID()),
        productName = ProductName(productName),
        unit = ProductUnit(unit),
        unitPrice = UnitPrice(unitPrice),
        stockQty = StockQuantity(stockQty),
    )

fun UpdateProductCommand.toProductName(): ProductName =
    ProductName(productName)

fun UpdateProductCommand.toProductUnit(): ProductUnit =
    ProductUnit(unit)

fun UpdateProductCommand.toUnitPrice(): UnitPrice =
    UnitPrice(unitPrice)

fun UpdateProductCommand.toStockQuantity(): StockQuantity =
    StockQuantity(stockQty)

package com.spoqa.hiringchallenge.application.product.mapper

import com.spoqa.hiringchallenge.application.product.dto.CreateProductCommand
import com.spoqa.hiringchallenge.domain.product.Product
import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import com.spoqa.hiringchallenge.domain.product.vo.ProductName
import com.spoqa.hiringchallenge.domain.product.vo.ProductUnit
import com.spoqa.hiringchallenge.domain.product.vo.StockQuantity
import com.spoqa.hiringchallenge.domain.product.vo.UnitPrice
import java.util.UUID

object ProductCommandMapper {
    fun toProduct(command: CreateProductCommand): Product =
        Product(
            productId = ProductId(UUID.randomUUID()),
            productName = ProductName(command.productName),
            unit = ProductUnit(command.unit),
            unitPrice = UnitPrice(command.unitPrice),
            stockQty = StockQuantity(command.stockQty),
        )
}

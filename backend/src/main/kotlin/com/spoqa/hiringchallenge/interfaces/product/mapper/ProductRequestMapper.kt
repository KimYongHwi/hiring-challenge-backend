package com.spoqa.hiringchallenge.interfaces.product.mapper

import com.spoqa.hiringchallenge.application.product.dto.CreateProductCommand
import com.spoqa.hiringchallenge.application.product.dto.UpdateProductCommand
import com.spoqa.hiringchallenge.interfaces.product.dto.CreateProductRequest
import com.spoqa.hiringchallenge.interfaces.product.dto.UpdateProductRequest
import java.util.UUID

fun CreateProductRequest.toCommand(): CreateProductCommand =
    CreateProductCommand(
        productName = productName,
        unit = unit,
        unitPrice = unitPrice,
        stockQty = stockQty,
    )

fun UpdateProductRequest.toCommand(productId: UUID): UpdateProductCommand =
    UpdateProductCommand(
        productId = productId,
        productName = productName,
        unit = unit,
        unitPrice = unitPrice,
        stockQty = stockQty,
    )

package com.spoqa.hiringchallenge.application.product

import com.spoqa.hiringchallenge.application.product.dto.CreateProductCommand
import com.spoqa.hiringchallenge.application.product.dto.DeleteProductCommand
import com.spoqa.hiringchallenge.application.product.dto.FindProductQuery
import com.spoqa.hiringchallenge.application.product.dto.FindProductsQuery
import com.spoqa.hiringchallenge.application.product.dto.PageResult
import com.spoqa.hiringchallenge.application.product.dto.ProductResult
import com.spoqa.hiringchallenge.application.product.dto.UpdateProductCommand

interface ProductUseCase {
    fun createProduct(command: CreateProductCommand): ProductResult

    fun findProduct(query: FindProductQuery): ProductResult

    fun findProducts(query: FindProductsQuery): PageResult<ProductResult>

    fun updateProduct(command: UpdateProductCommand): ProductResult

    fun deleteProduct(command: DeleteProductCommand)
}

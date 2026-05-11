package com.spoqa.hiringchallenge.application.product

interface ProductUseCase {
    fun createProduct(command: CreateProductCommand): ProductResult

    fun findProduct(query: FindProductQuery): ProductResult

    fun findProducts(query: FindProductsQuery): PageResult<ProductResult>

    fun updateProduct(command: UpdateProductCommand): ProductResult

    fun deleteProduct(command: DeleteProductCommand)
}

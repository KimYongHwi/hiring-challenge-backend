package com.spoqa.hiringchallenge.domain.product

import com.spoqa.hiringchallenge.domain.product.vo.ProductId

interface ProductRepository {
    fun save(product: Product): Product

    fun findById(productId: ProductId): Product?

    fun findAll(page: Int, size: Int): ProductPage

    fun deleteById(productId: ProductId)
}

data class ProductPage(
    val content: List<Product>,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
)

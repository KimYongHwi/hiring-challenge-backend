package com.spoqa.hiringchallenge.domain.product

import com.spoqa.hiringchallenge.domain.product.vo.ProductId

interface ProductRepository {
    fun save(product: Product): Product

    fun findById(productId: ProductId): Product?

    fun findByIdForUpdate(productId: ProductId): Product?

    fun findAll(page: Int, size: Int): ProductPage

    fun deleteById(productId: ProductId)
}

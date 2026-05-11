package com.spoqa.hiringchallenge.infrastructure.persistence.product

import com.spoqa.hiringchallenge.domain.product.Product
import com.spoqa.hiringchallenge.domain.product.ProductPage
import com.spoqa.hiringchallenge.domain.product.ProductRepository
import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryAdapter(
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun save(product: Product): Product =
        productJpaRepository.save(product.toJpaEntity()).toDomain()

    override fun findById(productId: ProductId): Product? =
        productJpaRepository.findById(productId.value)
            .map { it.toDomain() }
            .orElse(null)

    override fun findAll(page: Int, size: Int): ProductPage {
        val productPage = productJpaRepository.findAll(PageRequest.of(page, size))

        return ProductPage(
            content = productPage.content.map { it.toDomain() },
            totalElements = productPage.totalElements,
            totalPages = productPage.totalPages,
            number = productPage.number,
            size = productPage.size,
            first = productPage.isFirst,
            last = productPage.isLast,
        )
    }

    override fun deleteById(productId: ProductId) {
        productJpaRepository.deleteById(productId.value)
    }
}

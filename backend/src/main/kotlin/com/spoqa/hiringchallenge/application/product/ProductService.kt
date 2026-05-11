package com.spoqa.hiringchallenge.application.product

import com.spoqa.hiringchallenge.application.product.dto.CreateProductCommand
import com.spoqa.hiringchallenge.application.product.dto.DeleteProductCommand
import com.spoqa.hiringchallenge.application.product.dto.FindProductQuery
import com.spoqa.hiringchallenge.application.product.dto.FindProductsQuery
import com.spoqa.hiringchallenge.application.product.dto.PageResult
import com.spoqa.hiringchallenge.application.product.dto.ProductResult
import com.spoqa.hiringchallenge.application.product.dto.UpdateProductCommand
import com.spoqa.hiringchallenge.application.product.mapper.toPageResult
import com.spoqa.hiringchallenge.application.product.mapper.toProduct
import com.spoqa.hiringchallenge.application.product.mapper.toProductName
import com.spoqa.hiringchallenge.application.product.mapper.toProductUnit
import com.spoqa.hiringchallenge.application.product.mapper.toResult
import com.spoqa.hiringchallenge.application.product.mapper.toStockQuantity
import com.spoqa.hiringchallenge.application.product.mapper.toUnitPrice
import com.spoqa.hiringchallenge.domain.product.ProductRepository
import com.spoqa.hiringchallenge.domain.product.exception.ProductNotFoundException
import com.spoqa.hiringchallenge.domain.product.vo.ProductId

class ProductService(
    private val productRepository: ProductRepository,
) : ProductUseCase {
    override fun createProduct(command: CreateProductCommand): ProductResult {
        val product = command.toProduct()
        val savedProduct = productRepository.save(product)

        return savedProduct.toResult()
    }

    override fun findProduct(query: FindProductQuery): ProductResult {
        val productId = ProductId(query.productId)
        val product = productRepository.findById(productId)
            ?: throw ProductNotFoundException(productId)

        return product.toResult()
    }

    override fun findProducts(query: FindProductsQuery): PageResult<ProductResult> {
        val productPage = productRepository.findAll(
            page = query.page,
            size = query.size,
        )

        return productPage.toPageResult()
    }

    override fun updateProduct(command: UpdateProductCommand): ProductResult {
        val productId = ProductId(command.productId)
        val product = productRepository.findById(productId)
            ?: throw ProductNotFoundException(productId)

        val updatedProduct = product.update(
            productName = command.toProductName(),
            unit = command.toProductUnit(),
            unitPrice = command.toUnitPrice(),
            stockQty = command.toStockQuantity(),
        )
        val savedProduct = productRepository.save(updatedProduct)

        return savedProduct.toResult()
    }

    override fun deleteProduct(command: DeleteProductCommand) {
        val productId = ProductId(command.productId)
        productRepository.findById(productId)
            ?: throw ProductNotFoundException(productId)

        productRepository.deleteById(productId)
    }
}

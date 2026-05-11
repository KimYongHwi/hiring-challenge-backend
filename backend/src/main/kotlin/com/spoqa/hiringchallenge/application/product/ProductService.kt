package com.spoqa.hiringchallenge.application.product

import java.util.UUID

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
import com.spoqa.hiringchallenge.domain.product.Product
import com.spoqa.hiringchallenge.domain.product.ProductRepository
import com.spoqa.hiringchallenge.domain.product.exception.ProductNotFoundException
import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import com.spoqa.hiringchallenge.domain.product.vo.StockQuantity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
) : ProductUseCase {

    @Transactional
    override fun createProduct(command: CreateProductCommand): ProductResult {
        val product = command.toProduct()
        val savedProduct = productRepository.save(product)

        return savedProduct.toResult()
    }

    @Transactional(readOnly = true)
    override fun findProduct(query: FindProductQuery): ProductResult {
        val productId = ProductId(query.productId)
        val product = productRepository.findById(productId)
            ?: throw ProductNotFoundException(productId)

        return product.toResult()
    }

    @Transactional(readOnly = true)
    override fun findProducts(query: FindProductsQuery): PageResult<ProductResult> {
        val productPage = productRepository.findAll(
            page = query.page,
            size = query.size,
        )

        return productPage.toPageResult()
    }

    @Transactional
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

    @Transactional
    override fun deleteProduct(command: DeleteProductCommand) {
        val productId = ProductId(command.productId)
        productRepository.findById(productId)
            ?: throw ProductNotFoundException(productId)

        productRepository.deleteById(productId)
    }

    @Transactional
    override fun updateStock(productId: UUID, quantity: Int): ProductResult {
        val id = ProductId(productId)
        val product = productRepository.findByIdForUpdate(id)
            ?: throw ProductNotFoundException(id)

        val updatedProduct = if (quantity >= 0) {
            product.increaseStock(StockQuantity(quantity))
        } else {
            product.decreaseStock(StockQuantity(-quantity))
        }

        return productRepository.save(updatedProduct).toResult()
    }
}

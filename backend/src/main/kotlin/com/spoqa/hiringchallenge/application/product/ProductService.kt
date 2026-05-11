package com.spoqa.hiringchallenge.application.product

import com.spoqa.hiringchallenge.application.product.dto.CreateProductCommand
import com.spoqa.hiringchallenge.application.product.dto.DeleteProductCommand
import com.spoqa.hiringchallenge.application.product.dto.FindProductQuery
import com.spoqa.hiringchallenge.application.product.dto.FindProductsQuery
import com.spoqa.hiringchallenge.application.product.dto.PageResult
import com.spoqa.hiringchallenge.application.product.dto.ProductResult
import com.spoqa.hiringchallenge.application.product.dto.UpdateProductCommand
import com.spoqa.hiringchallenge.application.product.mapper.ProductCommandMapper
import com.spoqa.hiringchallenge.application.product.mapper.ProductResultMapper
import com.spoqa.hiringchallenge.domain.product.ProductRepository
import com.spoqa.hiringchallenge.domain.product.vo.ProductId

class ProductService(
    private val productRepository: ProductRepository,
) : ProductUseCase {
    override fun createProduct(command: CreateProductCommand): ProductResult {
        val product = ProductCommandMapper.toProduct(command)
        val savedProduct = productRepository.save(product)

        return ProductResultMapper.toResult(savedProduct)
    }

    override fun findProduct(query: FindProductQuery): ProductResult {
        val product = productRepository.findById(ProductId(query.productId))
            ?: throw NoSuchElementException("상품을 찾을 수 없습니다. productId=${query.productId}")

        return ProductResultMapper.toResult(product)
    }

    override fun findProducts(query: FindProductsQuery): PageResult<ProductResult> {
        val productPage = productRepository.findAll(
            page = query.page,
            size = query.size,
        )

        return ProductResultMapper.toPageResult(productPage)
    }

    override fun updateProduct(command: UpdateProductCommand): ProductResult {
        throw UnsupportedOperationException("상품 수정 usecase는 아직 구현되지 않았습니다.")
    }

    override fun deleteProduct(command: DeleteProductCommand) {
        throw UnsupportedOperationException("상품 삭제 usecase는 아직 구현되지 않았습니다.")
    }
}

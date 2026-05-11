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
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
) : ProductUseCase {
    override fun createProduct(command: CreateProductCommand): ProductResult {
        val product = ProductCommandMapper.toProduct(command)
        val savedProduct = productRepository.save(product)

        return ProductResultMapper.toResult(savedProduct)
    }

    override fun findProduct(query: FindProductQuery): ProductResult {
        throw UnsupportedOperationException("상품 단건 조회 usecase는 아직 구현되지 않았습니다.")
    }

    override fun findProducts(query: FindProductsQuery): PageResult<ProductResult> {
        throw UnsupportedOperationException("상품 목록 조회 usecase는 아직 구현되지 않았습니다.")
    }

    override fun updateProduct(command: UpdateProductCommand): ProductResult {
        throw UnsupportedOperationException("상품 수정 usecase는 아직 구현되지 않았습니다.")
    }

    override fun deleteProduct(command: DeleteProductCommand) {
        throw UnsupportedOperationException("상품 삭제 usecase는 아직 구현되지 않았습니다.")
    }
}

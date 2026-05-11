package com.spoqa.hiringchallenge.interfaces.product

import com.spoqa.hiringchallenge.application.product.ProductUseCase
import com.spoqa.hiringchallenge.application.product.dto.DeleteProductCommand
import com.spoqa.hiringchallenge.application.product.dto.FindProductQuery
import com.spoqa.hiringchallenge.application.product.dto.FindProductsQuery
import com.spoqa.hiringchallenge.application.product.dto.PageResult
import com.spoqa.hiringchallenge.interfaces.product.dto.CreateProductRequest
import com.spoqa.hiringchallenge.interfaces.product.dto.ProductResponse
import com.spoqa.hiringchallenge.interfaces.product.dto.UpdateProductRequest
import com.spoqa.hiringchallenge.interfaces.product.mapper.toCommand
import com.spoqa.hiringchallenge.interfaces.product.mapper.toProductResponsePage
import com.spoqa.hiringchallenge.interfaces.product.mapper.toResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productUseCase: ProductUseCase,
) {
    @GetMapping
    fun findProducts(
        @RequestParam page: Int,
        @RequestParam size: Int,
    ): PageResult<ProductResponse> =
        productUseCase.findProducts(
            FindProductsQuery(
                page = page,
                size = size,
            ),
        ).toProductResponsePage()

    @GetMapping("/{productId}")
    fun findProduct(
        @PathVariable productId: UUID,
    ): ProductResponse =
        productUseCase.findProduct(
            FindProductQuery(productId = productId),
        ).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(
        @RequestBody @Valid request: CreateProductRequest,
    ): ProductResponse =
        productUseCase.createProduct(request.toCommand()).toResponse()

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: UUID,
        @RequestBody @Valid request: UpdateProductRequest,
    ): ProductResponse =
        productUseCase.updateProduct(request.toCommand(productId)).toResponse()

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(
        @PathVariable productId: UUID,
    ) {
        productUseCase.deleteProduct(DeleteProductCommand(productId = productId))
    }
}

package com.spoqa.hiringchallenge.interfaces.product.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateProductRequest(
    @field:NotBlank(message = "상품명은 비어 있을 수 없습니다.")
    @field:Size(max = 100, message = "상품명은 1자 이상 100자 이하여야 합니다.")
    val productName: String,
    @field:NotBlank(message = "단위는 비어 있을 수 없습니다.")
    @field:Size(max = 30, message = "단위는 1자 이상 30자 이하여야 합니다.")
    val unit: String,
    @field:Min(value = 1, message = "단가는 1원 이상 1000000000원 이하여야 합니다.")
    val unitPrice: Long,
    @field:Min(value = 0, message = "재고 수량은 0개 이상 1000000개 이하여야 합니다.")
    val stockQty: Int,
)

data class UpdateProductRequest(
    @field:NotBlank(message = "상품명은 비어 있을 수 없습니다.")
    @field:Size(max = 100, message = "상품명은 1자 이상 100자 이하여야 합니다.")
    val productName: String,
    @field:NotBlank(message = "단위는 비어 있을 수 없습니다.")
    @field:Size(max = 30, message = "단위는 1자 이상 30자 이하여야 합니다.")
    val unit: String,
    @field:Min(value = 1, message = "단가는 1원 이상 1000000000원 이하여야 합니다.")
    val unitPrice: Long,
    @field:Min(value = 0, message = "재고 수량은 0개 이상 1000000개 이하여야 합니다.")
    val stockQty: Int,
)

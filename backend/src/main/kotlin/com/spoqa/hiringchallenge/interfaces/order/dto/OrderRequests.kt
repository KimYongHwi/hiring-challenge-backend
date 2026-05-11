package com.spoqa.hiringchallenge.interfaces.order.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateOrderRequest(
    @field:NotBlank(message = "주문자명은 비어 있을 수 없습니다.")
    @field:Size(max = 50, message = "주문자명은 1자 이상 50자 이하여야 합니다.")
    val ordererName: String,
    @field:NotBlank(message = "주소는 비어 있을 수 없습니다.")
    @field:Size(max = 255, message = "주소는 1자 이상 255자 이하여야 합니다.")
    val address: String,
    @field:NotBlank(message = "전화번호는 비어 있을 수 없습니다.")
    @field:Size(min = 7, max = 20, message = "전화번호는 7자 이상 20자 이하여야 합니다.")
    val phoneNo: String,
    @field:Valid
    @field:Size(min = 1, message = "주문 줄은 1개 이상이어야 합니다.")
    val orderLines: List<CreateOrderLineRequest>,
)

data class CreateOrderLineRequest(
    val productId: UUID,
    @field:Min(value = 1, message = "주문 수량은 1개 이상 1000000개 이하여야 합니다.")
    val qty: Int,
)

data class UpdateOrderRequest(
    @field:NotBlank(message = "주문자명은 비어 있을 수 없습니다.")
    @field:Size(max = 50, message = "주문자명은 1자 이상 50자 이하여야 합니다.")
    val ordererName: String,
    @field:NotBlank(message = "주소는 비어 있을 수 없습니다.")
    @field:Size(max = 255, message = "주소는 1자 이상 255자 이하여야 합니다.")
    val address: String,
    @field:NotBlank(message = "전화번호는 비어 있을 수 없습니다.")
    @field:Size(min = 7, max = 20, message = "전화번호는 7자 이상 20자 이하여야 합니다.")
    val phoneNo: String,
    @field:Valid
    @field:Size(min = 1, message = "주문 줄은 1개 이상이어야 합니다.")
    val orderLines: List<UpdateOrderLineRequest>,
)

data class UpdateOrderLineRequest(
    val productId: UUID,
    @field:Min(value = 1, message = "주문 수량은 1개 이상 1000000개 이하여야 합니다.")
    val qty: Int,
)

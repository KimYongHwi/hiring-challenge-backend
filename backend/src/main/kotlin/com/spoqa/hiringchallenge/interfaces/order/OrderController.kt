package com.spoqa.hiringchallenge.interfaces.order

import com.spoqa.hiringchallenge.application.order.OrderUseCase
import com.spoqa.hiringchallenge.application.order.dto.DeleteOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.FindOrderQuery
import com.spoqa.hiringchallenge.application.order.dto.FindOrdersQuery
import com.spoqa.hiringchallenge.application.order.dto.PageResult
import com.spoqa.hiringchallenge.interfaces.order.dto.CreateOrderRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.OrderDetailResponse
import com.spoqa.hiringchallenge.interfaces.order.dto.OrderListItemResponse
import com.spoqa.hiringchallenge.interfaces.order.dto.UpdateOrderRequest
import com.spoqa.hiringchallenge.interfaces.order.mapper.toCommand
import com.spoqa.hiringchallenge.interfaces.order.mapper.toOrderListItemResponsePage
import com.spoqa.hiringchallenge.interfaces.order.mapper.toResponse
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
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderUseCase: OrderUseCase,
) {
    @GetMapping
    fun findOrders(
        @RequestParam page: Int,
        @RequestParam size: Int,
    ): PageResult<OrderListItemResponse> =
        orderUseCase.findOrders(
            FindOrdersQuery(
                page = page,
                size = size,
            ),
        ).toOrderListItemResponsePage()

    @GetMapping("/{orderId}")
    fun findOrder(
        @PathVariable orderId: UUID,
    ): OrderDetailResponse =
        orderUseCase.findOrder(
            FindOrderQuery(orderId = orderId),
        ).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @RequestBody @Valid request: CreateOrderRequest,
    ): OrderDetailResponse =
        orderUseCase.createOrder(request.toCommand()).toResponse()

    @PutMapping("/{orderId}")
    fun updateOrder(
        @PathVariable orderId: UUID,
        @RequestBody @Valid request: UpdateOrderRequest,
    ): OrderDetailResponse =
        orderUseCase.updateOrder(request.toCommand(orderId)).toResponse()

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrder(
        @PathVariable orderId: UUID,
    ) {
        orderUseCase.deleteOrder(DeleteOrderCommand(orderId = orderId))
    }
}

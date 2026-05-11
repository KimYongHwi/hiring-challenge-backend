package com.spoqa.hiringchallenge.application.order

import com.spoqa.hiringchallenge.application.order.dto.CreateOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.DeleteOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.FindOrderQuery
import com.spoqa.hiringchallenge.application.order.dto.FindOrdersQuery
import com.spoqa.hiringchallenge.application.order.dto.OrderDetailResult
import com.spoqa.hiringchallenge.application.order.dto.OrderListItemResult
import com.spoqa.hiringchallenge.application.order.dto.PageResult
import com.spoqa.hiringchallenge.application.order.dto.UpdateOrderCommand
import com.spoqa.hiringchallenge.application.order.mapper.toDetailResult
import com.spoqa.hiringchallenge.application.order.mapper.toOrder
import com.spoqa.hiringchallenge.application.order.mapper.toOrderLine
import com.spoqa.hiringchallenge.application.order.mapper.toProductId
import com.spoqa.hiringchallenge.domain.order.OrderRepository
import com.spoqa.hiringchallenge.domain.product.ProductRepository
import com.spoqa.hiringchallenge.domain.product.exception.ProductNotFoundException

class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
) : OrderUseCase {
    override fun createOrder(command: CreateOrderCommand): OrderDetailResult {
        val orderLines = command.orderLines.map { orderLineCommand ->
            val productId = orderLineCommand.toProductId()
            val product = productRepository.findById(productId)
                ?: throw ProductNotFoundException(productId)

            orderLineCommand.toOrderLine(product)
        }
        val order = command.toOrder(orderLines)
        val savedOrder = orderRepository.save(order)

        return savedOrder.toDetailResult()
    }

    override fun findOrder(query: FindOrderQuery): OrderDetailResult {
        throw UnsupportedOperationException("주문 단건 조회 usecase는 아직 구현되지 않았습니다.")
    }

    override fun findOrders(query: FindOrdersQuery): PageResult<OrderListItemResult> {
        throw UnsupportedOperationException("주문 목록 조회 usecase는 아직 구현되지 않았습니다.")
    }

    override fun updateOrder(command: UpdateOrderCommand): OrderDetailResult {
        throw UnsupportedOperationException("주문 수정 usecase는 아직 구현되지 않았습니다.")
    }

    override fun deleteOrder(command: DeleteOrderCommand) {
        throw UnsupportedOperationException("주문 삭제 usecase는 아직 구현되지 않았습니다.")
    }
}

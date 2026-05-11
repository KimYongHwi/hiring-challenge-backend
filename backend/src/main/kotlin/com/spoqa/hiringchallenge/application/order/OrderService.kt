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
import com.spoqa.hiringchallenge.application.order.mapper.toOrderAddress
import com.spoqa.hiringchallenge.application.order.mapper.toOrderLine
import com.spoqa.hiringchallenge.application.order.mapper.toOrdererName
import com.spoqa.hiringchallenge.application.order.mapper.toPageResult
import com.spoqa.hiringchallenge.application.order.mapper.toPhoneNo
import com.spoqa.hiringchallenge.application.order.mapper.toProductId
import com.spoqa.hiringchallenge.domain.order.exception.OrderNotFoundException
import com.spoqa.hiringchallenge.domain.order.OrderRepository
import com.spoqa.hiringchallenge.domain.order.vo.OrderId
import com.spoqa.hiringchallenge.domain.product.ProductRepository
import com.spoqa.hiringchallenge.domain.product.exception.ProductNotFoundException
import org.springframework.stereotype.Service

@Service
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
        val orderId = OrderId(query.orderId)
        val order = orderRepository.findById(orderId)
            ?: throw OrderNotFoundException(orderId)

        return order.toDetailResult()
    }

    override fun findOrders(query: FindOrdersQuery): PageResult<OrderListItemResult> {
        val orderPage = orderRepository.findAll(
            page = query.page,
            size = query.size,
        )

        return orderPage.toPageResult()
    }

    override fun updateOrder(command: UpdateOrderCommand): OrderDetailResult {
        val orderId = OrderId(command.orderId)
        val order = orderRepository.findById(orderId)
            ?: throw OrderNotFoundException(orderId)
        val orderLines = command.orderLines.map { orderLineCommand ->
            val productId = orderLineCommand.toProductId()
            val product = productRepository.findById(productId)
                ?: throw ProductNotFoundException(productId)

            orderLineCommand.toOrderLine(product)
        }
        val updatedOrder = order.update(
            ordererName = command.toOrdererName(),
            address = command.toOrderAddress(),
            phoneNo = command.toPhoneNo(),
            orderLines = orderLines,
        )
        val savedOrder = orderRepository.save(updatedOrder)

        return savedOrder.toDetailResult()
    }

    override fun deleteOrder(command: DeleteOrderCommand) {
        val orderId = OrderId(command.orderId)
        orderRepository.findById(orderId)
            ?: throw OrderNotFoundException(orderId)

        orderRepository.deleteById(orderId)
    }
}

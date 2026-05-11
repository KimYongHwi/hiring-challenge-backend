package com.spoqa.hiringchallenge.application.order

import com.spoqa.hiringchallenge.application.order.dto.CreateOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.DeleteOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.FindOrderQuery
import com.spoqa.hiringchallenge.application.order.dto.FindOrdersQuery
import com.spoqa.hiringchallenge.application.order.dto.OrderDetailResult
import com.spoqa.hiringchallenge.application.order.dto.OrderListItemResult
import com.spoqa.hiringchallenge.application.order.dto.PageResult
import com.spoqa.hiringchallenge.application.order.dto.UpdateOrderCommand
import com.spoqa.hiringchallenge.application.product.ProductUseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderFacade(
    private val orderUseCase: OrderUseCase,
    private val productUseCase: ProductUseCase,
) {
    @Transactional
    fun createOrder(command: CreateOrderCommand): OrderDetailResult {
        command.orderLines.forEach {
            productUseCase.updateStock(it.productId, -it.qty)
        }
        return orderUseCase.createOrder(command)
    }

    @Transactional
    fun updateOrder(command: UpdateOrderCommand): OrderDetailResult {
        val existingOrder = orderUseCase.findOrder(FindOrderQuery(command.orderId))

        // Restore stock
        existingOrder.orderLines.forEach {
            productUseCase.updateStock(it.productId, it.qty)
        }

        // Decrease new stock
        command.orderLines.forEach {
            productUseCase.updateStock(it.productId, -it.qty)
        }

        return orderUseCase.updateOrder(command)
    }

    @Transactional
    fun deleteOrder(command: DeleteOrderCommand) {
        val existingOrder = orderUseCase.findOrder(FindOrderQuery(command.orderId))

        // Restore stock
        existingOrder.orderLines.forEach {
            productUseCase.updateStock(it.productId, it.qty)
        }

        orderUseCase.deleteOrder(command)
    }

    fun findOrder(query: FindOrderQuery): OrderDetailResult =
        orderUseCase.findOrder(query)

    fun findOrders(query: FindOrdersQuery): PageResult<OrderListItemResult> =
        orderUseCase.findOrders(query)
}

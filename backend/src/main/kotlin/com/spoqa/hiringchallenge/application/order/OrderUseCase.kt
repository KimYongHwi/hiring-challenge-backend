package com.spoqa.hiringchallenge.application.order

import com.spoqa.hiringchallenge.application.order.dto.CreateOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.DeleteOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.FindOrderQuery
import com.spoqa.hiringchallenge.application.order.dto.FindOrdersQuery
import com.spoqa.hiringchallenge.application.order.dto.OrderDetailResult
import com.spoqa.hiringchallenge.application.order.dto.OrderListItemResult
import com.spoqa.hiringchallenge.application.order.dto.PageResult
import com.spoqa.hiringchallenge.application.order.dto.UpdateOrderCommand

interface OrderUseCase {
    fun createOrder(command: CreateOrderCommand): OrderDetailResult

    fun findOrder(query: FindOrderQuery): OrderDetailResult

    fun findOrders(query: FindOrdersQuery): PageResult<OrderListItemResult>

    fun updateOrder(command: UpdateOrderCommand): OrderDetailResult

    fun deleteOrder(command: DeleteOrderCommand)
}

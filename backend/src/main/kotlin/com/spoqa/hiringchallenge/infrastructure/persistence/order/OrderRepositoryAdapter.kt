package com.spoqa.hiringchallenge.infrastructure.persistence.order

import com.spoqa.hiringchallenge.domain.order.Order
import com.spoqa.hiringchallenge.domain.order.OrderPage
import com.spoqa.hiringchallenge.domain.order.OrderRepository
import com.spoqa.hiringchallenge.domain.order.vo.OrderId
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryAdapter(
    private val orderJpaRepository: OrderJpaRepository,
    private val orderLineJpaRepository: OrderLineJpaRepository,
) : OrderRepository {
    override fun save(order: Order): Order {
        val orderEntity = orderJpaRepository.findById(order.orderId.value)
            .map { existingOrder ->
                existingOrder.ordererName = order.ordererName.value
                existingOrder.address = order.address.value
                existingOrder.phoneNo = order.phoneNo.value
                existingOrder.orderLines.clear()
                existingOrder.orderLines.addAll(
                    order.orderLines.map { it.toJpaEntity(existingOrder) },
                )
                existingOrder
            }
            .orElseGet { order.toJpaEntity() }
        val savedOrder = orderJpaRepository.save(orderEntity)

        return savedOrder.toDomain()
    }

    override fun findById(orderId: OrderId): Order? =
        orderJpaRepository.findById(orderId.value)
            .map { it.toDomain() }
            .orElse(null)

    override fun findAll(page: Int, size: Int): OrderPage {
        val orderPage = orderJpaRepository.findAll(PageRequest.of(page, size))

        return OrderPage(
            content = orderPage.content.map { it.toDomain() },
            totalElements = orderPage.totalElements,
            totalPages = orderPage.totalPages,
            number = orderPage.number,
            size = orderPage.size,
            first = orderPage.isFirst,
            last = orderPage.isLast,
        )
    }

    override fun deleteById(orderId: OrderId) {
        val orderEntity = orderJpaRepository.findById(orderId.value).orElse(null) ?: return
        orderLineJpaRepository.deleteAll(orderEntity.orderLines)
        orderJpaRepository.delete(orderEntity)
    }
}

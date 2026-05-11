package com.spoqa.hiringchallenge.interfaces.order.fixture

import com.spoqa.hiringchallenge.interfaces.order.dto.CreateOrderLineRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.CreateOrderRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.UpdateOrderLineRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.UpdateOrderRequest
import java.util.UUID

object OrderRequestFixtures {
    fun createOrderRequest(
        ordererName: String = "홍길동",
        address: String = "서울시 중구",
        phoneNo: String = "010-1234-5678",
        orderLines: List<CreateOrderLineRequest> = listOf(createOrderLineRequest()),
    ): CreateOrderRequest =
        CreateOrderRequest(
            ordererName = ordererName,
            address = address,
            phoneNo = phoneNo,
            orderLines = orderLines,
        )

    fun createOrderLineRequest(
        productId: UUID = UUID.randomUUID(),
        qty: Int = 1,
    ): CreateOrderLineRequest =
        CreateOrderLineRequest(
            productId = productId,
            qty = qty,
        )

    fun updateOrderRequest(
        ordererName: String = "김철수",
        address: String = "서울시 강남구",
        phoneNo: String = "010-9999-8888",
        orderLines: List<UpdateOrderLineRequest> = listOf(updateOrderLineRequest()),
    ): UpdateOrderRequest =
        UpdateOrderRequest(
            ordererName = ordererName,
            address = address,
            phoneNo = phoneNo,
            orderLines = orderLines,
        )

    fun updateOrderLineRequest(
        productId: UUID = UUID.randomUUID(),
        qty: Int = 1,
    ): UpdateOrderLineRequest =
        UpdateOrderLineRequest(
            productId = productId,
            qty = qty,
        )
}

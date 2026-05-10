package com.spoqa.hiringchallenge.domain.order

import com.spoqa.hiringchallenge.domain.order.vo.OrderAddress
import com.spoqa.hiringchallenge.domain.order.vo.OrderId
import com.spoqa.hiringchallenge.domain.order.vo.OrdererName
import com.spoqa.hiringchallenge.domain.order.vo.PhoneNo

data class Order(
    val orderId: OrderId,
    val ordererName: OrdererName,
    val address: OrderAddress,
    val phoneNo: PhoneNo,
    val orderLines: List<OrderLine>,
) {
    init {
        require(orderLines.isNotEmpty()) { "주문 줄은 1개 이상이어야 합니다." }
    }

    fun lineItemCount(): Int = orderLines.size

    fun totalAmount(): Long = orderLines.sumOf { it.totalAmount() }

    fun update(
        ordererName: OrdererName,
        address: OrderAddress,
        phoneNo: PhoneNo,
        orderLines: List<OrderLine>,
    ): Order =
        copy(
            ordererName = ordererName,
            address = address,
            phoneNo = phoneNo,
            orderLines = orderLines,
        )
}

package com.spoqa.hiringchallenge.domain.order.exception

import com.spoqa.hiringchallenge.domain.exception.NotFoundException
import com.spoqa.hiringchallenge.domain.order.vo.OrderId

class OrderNotFoundException(
    orderId: OrderId,
) : NotFoundException(
    code = "ORDER_NOT_FOUND",
    message = "주문을 찾을 수 없습니다. orderId=${orderId.value}",
)

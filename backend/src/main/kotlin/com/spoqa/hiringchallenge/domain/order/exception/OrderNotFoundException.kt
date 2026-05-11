package com.spoqa.hiringchallenge.domain.order.exception

import com.spoqa.hiringchallenge.domain.order.vo.OrderId

class OrderNotFoundException(
    orderId: OrderId,
) : RuntimeException("주문을 찾을 수 없습니다. orderId=${orderId.value}")

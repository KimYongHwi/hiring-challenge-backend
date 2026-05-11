package com.spoqa.hiringchallenge.interfaces.order.mapper

import com.spoqa.hiringchallenge.application.order.dto.CreateOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.CreateOrderLineCommand
import com.spoqa.hiringchallenge.application.order.dto.UpdateOrderCommand
import com.spoqa.hiringchallenge.application.order.dto.UpdateOrderLineCommand
import com.spoqa.hiringchallenge.interfaces.order.dto.CreateOrderLineRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.CreateOrderRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.UpdateOrderLineRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.UpdateOrderRequest
import java.util.UUID

fun CreateOrderRequest.toCommand(): CreateOrderCommand =
    CreateOrderCommand(
        ordererName = ordererName,
        address = address,
        phoneNo = phoneNo,
        orderLines = orderLines.map { it.toCommand() },
    )

fun CreateOrderLineRequest.toCommand(): CreateOrderLineCommand =
    CreateOrderLineCommand(
        productId = productId,
        qty = qty,
    )

fun UpdateOrderRequest.toCommand(orderId: UUID): UpdateOrderCommand =
    UpdateOrderCommand(
        orderId = orderId,
        ordererName = ordererName,
        address = address,
        phoneNo = phoneNo,
        orderLines = orderLines.map { it.toCommand() },
    )

fun UpdateOrderLineRequest.toCommand(): UpdateOrderLineCommand =
    UpdateOrderLineCommand(
        productId = productId,
        qty = qty,
    )

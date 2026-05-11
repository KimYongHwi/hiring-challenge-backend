package com.spoqa.hiringchallenge.interfaces.order.fixture

import com.fasterxml.jackson.databind.ObjectMapper
import com.spoqa.hiringchallenge.interfaces.order.fixture.OrderRequestFixtures.createOrderLineRequest
import com.spoqa.hiringchallenge.interfaces.order.fixture.OrderRequestFixtures.createOrderRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.CreateOrderLineRequest
import com.spoqa.hiringchallenge.interfaces.order.dto.CreateOrderRequest
import com.spoqa.hiringchallenge.interfaces.product.fixture.ProductRequestFixtures.createProductRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

object OrderE2ETestSupport {
    fun createProduct(
        mockMvc: MockMvc,
        objectMapper: ObjectMapper,
        productName: String = "콜라",
        unit: String = "캔",
        unitPrice: Long = 1_500,
        stockQty: Int = 20,
    ): UUID {
        val result = mockMvc.perform(
            post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        createProductRequest(
                            productName = productName,
                            unit = unit,
                            unitPrice = unitPrice,
                            stockQty = stockQty,
                        ),
                    ),
                ),
        )
            .andExpect(status().isCreated)
            .andReturn()

        return UUID.fromString(
            objectMapper.readTree(result.response.contentAsString)
                .get("productId")
                .asText(),
        )
    }

    fun createOrder(
        mockMvc: MockMvc,
        objectMapper: ObjectMapper,
        request: CreateOrderRequest,
    ): UUID {
        val result = mockMvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isCreated)
            .andReturn()

        return UUID.fromString(
            objectMapper.readTree(result.response.contentAsString)
                .get("orderId")
                .asText(),
        )
    }

    fun createOrder(
        mockMvc: MockMvc,
        objectMapper: ObjectMapper,
        productId: UUID,
        qty: Int = 2,
    ): UUID =
        createOrder(
            mockMvc = mockMvc,
            objectMapper = objectMapper,
            request = createOrderRequest(
                orderLines = listOf(
                    createOrderLineRequest(
                        productId = productId,
                        qty = qty,
                    ),
                ),
            ),
        )

    fun orderLine(
        productId: UUID,
        qty: Int = 1,
    ): CreateOrderLineRequest =
        createOrderLineRequest(
            productId = productId,
            qty = qty,
        )
}

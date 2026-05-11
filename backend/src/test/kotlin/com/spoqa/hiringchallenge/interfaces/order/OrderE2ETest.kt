package com.spoqa.hiringchallenge.interfaces.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.spoqa.hiringchallenge.interfaces.order.fixture.OrderE2ETestSupport.createOrder
import com.spoqa.hiringchallenge.interfaces.order.fixture.OrderE2ETestSupport.createProduct
import com.spoqa.hiringchallenge.interfaces.order.fixture.OrderRequestFixtures.createOrderLineRequest
import com.spoqa.hiringchallenge.interfaces.order.fixture.OrderRequestFixtures.createOrderRequest
import com.spoqa.hiringchallenge.interfaces.order.fixture.OrderRequestFixtures.updateOrderLineRequest
import com.spoqa.hiringchallenge.interfaces.order.fixture.OrderRequestFixtures.updateOrderRequest
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.annotation.DirtiesContext

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("OrderController E2E")
class OrderE2ETest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @Transactional
    @Nested
    @DisplayName("POST /api/v1/orders")
    inner class CreateOrder {
        @Test
        fun `주문을 생성한다`() {
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                unitPrice = 1_500,
            )
            val request = createOrderRequest(
                orderLines = listOf(
                    createOrderLineRequest(
                        productId = productId,
                        qty = 2,
                    ),
                ),
            )

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.ordererName").value("홍길동"))
                .andExpect(jsonPath("$.address").value("서울시 중구"))
                .andExpect(jsonPath("$.phoneNo").value("010-1234-5678"))
                .andExpect(jsonPath("$.orderLines[0].productId").value(productId.toString()))
                .andExpect(jsonPath("$.orderLines[0].qty").value(2))
                .andExpect(jsonPath("$.orderLines[0].unitPrice").value(1500))
        }

        @Test
        fun `주문자명이 비어 있으면 400을 응답한다`() {
            val request = createOrderRequest(ordererName = "   ")

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문자명은 비어 있을 수 없습니다."))
        }

        @Test
        fun `주소가 비어 있으면 400을 응답한다`() {
            val request = createOrderRequest(address = "   ")

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주소는 비어 있을 수 없습니다."))
        }

        @Test
        fun `전화번호가 비어 있으면 400을 응답한다`() {
            val request = createOrderRequest(phoneNo = "       ")

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("전화번호는 비어 있을 수 없습니다."))
        }

        @Test
        fun `존재하지 않는 상품으로 주문하면 404를 응답한다`() {
            val productId = UUID.randomUUID()
            val request = createOrderRequest(
                orderLines = listOf(
                    createOrderLineRequest(productId = productId),
                ),
            )

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다. productId=$productId"))
        }

        @Test
        fun `주문 줄이 비어 있으면 400을 응답한다`() {
            val request = createOrderRequest(orderLines = emptyList())

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문 줄은 1개 이상이어야 합니다."))
        }

        @Test
        fun `주문 수량이 0이면 400을 응답한다`() {
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
            )
            val request = createOrderRequest(
                orderLines = listOf(
                    createOrderLineRequest(
                        productId = productId,
                        qty = 0,
                    ),
                ),
            )

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문 수량은 1개 이상 1000000개 이하여야 합니다."))
        }

        @Test
        fun `주문 수량이 음수이면 400을 응답한다`() {
            val request = createOrderRequest(
                orderLines = listOf(
                    createOrderLineRequest(
                        productId = UUID.randomUUID(),
                        qty = -1,
                    ),
                ),
            )

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문 수량은 1개 이상 1000000개 이하여야 합니다."))
        }

        @Test
        fun `요청 본문이 JSON 형식이 아니면 400을 응답한다`() {
            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("invalid-json"),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("요청 본문을 읽을 수 없습니다."))
        }
    }

    @Transactional
    @Nested
    @DisplayName("GET /api/v1/orders/{orderId}")
    inner class FindOrder {
        @Test
        fun `주문을 단건 조회한다`() {
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                unitPrice = 2_000,
            )
            val orderId = createOrder(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productId = productId,
                qty = 3,
            )

            mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.orderId").value(orderId.toString()))
                .andExpect(jsonPath("$.ordererName").value("홍길동"))
                .andExpect(jsonPath("$.address").value("서울시 중구"))
                .andExpect(jsonPath("$.phoneNo").value("010-1234-5678"))
                .andExpect(jsonPath("$.orderLines[0].productId").value(productId.toString()))
                .andExpect(jsonPath("$.orderLines[0].qty").value(3))
                .andExpect(jsonPath("$.orderLines[0].unitPrice").value(2000))
        }

        @Test
        fun `존재하지 않는 주문이면 404를 응답한다`() {
            val orderId = UUID.randomUUID()

            mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("ORDER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("주문을 찾을 수 없습니다. orderId=$orderId"))
        }

        @Test
        fun `주문 ID가 UUID 형식이 아니면 400을 응답한다`() {
            mockMvc.perform(get("/api/v1/orders/{orderId}", "invalid-order-id"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 값입니다. name=orderId"))
        }
    }

    @Transactional
    @Nested
    @DisplayName("GET /api/v1/orders")
    inner class FindOrders {
        @Test
        fun `주문 목록을 조회한다`() {
            val colaProductId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productName = "콜라",
                unitPrice = 1_500,
            )
            val waterProductId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productName = "생수",
                unitPrice = 900,
            )

            createOrder(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                request = createOrderRequest(
                    ordererName = "홍길동",
                    orderLines = listOf(
                        createOrderLineRequest(
                            productId = colaProductId,
                            qty = 2,
                        ),
                    ),
                ),
            )
            createOrder(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                request = createOrderRequest(
                    ordererName = "김철수",
                    orderLines = listOf(
                        createOrderLineRequest(
                            productId = waterProductId,
                            qty = 3,
                        ),
                    ),
                ),
            )

            mockMvc.perform(get("/api/v1/orders?page=0&size=100"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.content[*].ordererName", hasItems("홍길동", "김철수")))
                .andExpect(jsonPath("$.content[*].lineItemCount", hasItem(1)))
                .andExpect(jsonPath("$.content[*].totalAmount", hasItems(3000, 2700)))
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(100))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").exists())
        }

        @Test
        fun `page 파라미터가 없으면 400을 응답한다`() {
            mockMvc.perform(get("/api/v1/orders?size=10"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("필수 요청 파라미터가 없습니다. name=page"))
        }

        @Test
        fun `page가 음수이면 400을 응답한다`() {
            mockMvc.perform(get("/api/v1/orders?page=-1&size=10"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Page index must not be less than zero"))
        }
    }

    @Transactional
    @Nested
    @DisplayName("PUT /api/v1/orders/{orderId}")
    inner class UpdateOrder {
        @Test
        fun `주문을 수정한다`() {
            val beforeProductId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productName = "콜라",
                unitPrice = 1_500,
            )
            val afterProductId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productName = "사이다",
                unitPrice = 1_800,
            )
            val orderId = createOrder(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productId = beforeProductId,
            )
            val request = updateOrderRequest(
                ordererName = "김철수",
                address = "서울시 강남구",
                phoneNo = "010-9999-8888",
                orderLines = listOf(
                    updateOrderLineRequest(
                        productId = afterProductId,
                        qty = 4,
                    ),
                ),
            )

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", orderId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.orderId").value(orderId.toString()))
                .andExpect(jsonPath("$.ordererName").value("김철수"))
                .andExpect(jsonPath("$.address").value("서울시 강남구"))
                .andExpect(jsonPath("$.phoneNo").value("010-9999-8888"))
                .andExpect(jsonPath("$.orderLines[0].productId").value(afterProductId.toString()))
                .andExpect(jsonPath("$.orderLines[0].qty").value(4))
                .andExpect(jsonPath("$.orderLines[0].unitPrice").value(1800))
        }

        @Test
        fun `존재하지 않는 주문을 수정하면 404를 응답한다`() {
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
            )
            val orderId = UUID.randomUUID()
            val request = updateOrderRequest(
                orderLines = listOf(
                    updateOrderLineRequest(productId = productId),
                ),
            )

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", orderId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("ORDER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("주문을 찾을 수 없습니다. orderId=$orderId"))
        }

        @Test
        fun `존재하지 않는 상품으로 주문을 수정하면 404를 응답한다`() {
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
            )
            val orderId = createOrder(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productId = productId,
            )
            val missingProductId = UUID.randomUUID()
            val request = updateOrderRequest(
                orderLines = listOf(
                    updateOrderLineRequest(productId = missingProductId),
                ),
            )

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", orderId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다. productId=$missingProductId"))
        }

        @Test
        fun `수정 요청의 주문자명이 비어 있으면 400을 응답한다`() {
            val request = updateOrderRequest(ordererName = "   ")

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문자명은 비어 있을 수 없습니다."))
        }

        @Test
        fun `수정 요청의 주소가 비어 있으면 400을 응답한다`() {
            val request = updateOrderRequest(address = "   ")

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주소는 비어 있을 수 없습니다."))
        }

        @Test
        fun `수정 요청의 전화번호가 비어 있으면 400을 응답한다`() {
            val request = updateOrderRequest(phoneNo = "       ")

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("전화번호는 비어 있을 수 없습니다."))
        }

        @Test
        fun `수정 요청의 전화번호 형식이 잘못되면 400을 응답한다`() {
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
            )
            val orderId = createOrder(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productId = productId,
            )
            val request = updateOrderRequest(
                phoneNo = "010-ABCD-1234",
                orderLines = listOf(
                    updateOrderLineRequest(productId = productId),
                ),
            )

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", orderId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("전화번호는 숫자와 하이픈만 포함할 수 있습니다."))
        }

        @Test
        fun `수정 요청의 주문 줄이 비어 있으면 400을 응답한다`() {
            val request = updateOrderRequest(orderLines = emptyList())

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문 줄은 1개 이상이어야 합니다."))
        }

        @Test
        fun `수정 요청의 주문 수량이 0이면 400을 응답한다`() {
            val request = updateOrderRequest(
                orderLines = listOf(
                    updateOrderLineRequest(
                        productId = UUID.randomUUID(),
                        qty = 0,
                    ),
                ),
            )

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문 수량은 1개 이상 1000000개 이하여야 합니다."))
        }

        @Test
        fun `수정 요청의 주문 수량이 음수이면 400을 응답한다`() {
            val request = updateOrderRequest(
                orderLines = listOf(
                    updateOrderLineRequest(
                        productId = UUID.randomUUID(),
                        qty = -1,
                    ),
                ),
            )

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문 수량은 1개 이상 1000000개 이하여야 합니다."))
        }

        @Test
        fun `주문 ID가 UUID 형식이 아니면 400을 응답한다`() {
            val request = updateOrderRequest()

            mockMvc.perform(
                put("/api/v1/orders/{orderId}", "invalid-order-id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 값입니다. name=orderId"))
        }
    }

    @Transactional
    @Nested
    @DisplayName("DELETE /api/v1/orders/{orderId}")
    inner class DeleteOrder {
        @Test
        fun `주문을 삭제한다`() {
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
            )
            val orderId = createOrder(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productId = productId,
            )

            mockMvc.perform(delete("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isNoContent)

            mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("ORDER_NOT_FOUND"))
        }

        @Test
        fun `존재하지 않는 주문을 삭제하면 404를 응답한다`() {
            val orderId = UUID.randomUUID()

            mockMvc.perform(delete("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("ORDER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("주문을 찾을 수 없습니다. orderId=$orderId"))
        }

        @Test
        fun `주문 ID가 UUID 형식이 아니면 400을 응답한다`() {
            mockMvc.perform(delete("/api/v1/orders/{orderId}", "invalid-order-id"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 값입니다. name=orderId"))
        }
    }
    
    @Nested
    @DisplayName("Edge Case Tests")
    inner class EdgeCaseTests {
        @Test
        fun `주문 생성 후 상품 가격이 변해도 기존 주문의 단가는 유지되어야 한다`() {
            // Given: 상품 생성 (단가 1000원)
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                unitPrice = 1000
            )
            // 주문 생성
            val orderId = createOrder(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productId = productId,
                qty = 5
            )

            // When: 상품 가격 수정 (2000원으로 변경)
            val updateRequest = mapOf(
                "productName" to "수정된 상품",
                "unit" to "개",
                "unitPrice" to 2000,
                "stockQty" to 100
            )
            mockMvc.perform(
                put("/api/v1/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest))
            ).andExpect(status().isOk)

            // Then: 기존 주문 조회 시 단가가 1000원이어야 함
            mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.orderLines[0].unitPrice").value(1000))
                
            // 목록 조회 시에도 총액이 유지되어야 함 (1000 * 5 = 5000)
            mockMvc.perform(get("/api/v1/orders?page=0&size=10"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.content[?(@.orderId=='$orderId')].totalAmount").value(5000))
        }

        @Test
        fun `재고가 부족하면 주문에 실패하고 400 에러를 반환한다`() {
            // Given: 재고가 10개인 상품 생성
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                stockQty = 10
            )

            // When: 11개 주문 시도
            val request = createOrderRequest(
                orderLines = listOf(
                    createOrderLineRequest(productId = productId, qty = 11)
                )
            )

            // Then: 400 Bad Request 및 에러 메시지 확인
            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("재고 수량이 부족합니다."))
        }

        @Test
        fun `주문 생성 중 재고 부족으로 실패하면 모든 변경사항이 롤백되어야 한다`() {
            // Given: 상품 A(재고 10), 상품 B(재고 0) 생성
            val productIdA = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productName = "상품A",
                stockQty = 10
            )
            val productIdB = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                productName = "상품B",
                stockQty = 0
            )

            // When: 상품 A(5개), 상품 B(1개) 주문 시도 -> B 때문에 실패 예상
            val request = createOrderRequest(
                orderLines = listOf(
                    createOrderLineRequest(productId = productIdA, qty = 5),
                    createOrderLineRequest(productId = productIdB, qty = 1)
                )
            )

            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isBadRequest)

            // Then: 상품 A의 재고가 여전히 10이어야 함 (차감된 5가 롤백됨)
            val productAResult = mockMvc.perform(get("/api/v1/products/{productId}", productIdA))
                .andExpect(status().isOk)
                .andReturn()

            val stockQtyA = objectMapper.readTree(productAResult.response.contentAsString)
                .get("stockQty")
                .asInt()

            assertThat(stockQtyA).isEqualTo(10)
        }
    }
    
    @Nested
    @DirtiesContext
    @DisplayName("Concurrency Tests")
    inner class ConcurrencyTests {
        @Test
        @DisplayName("동시에 100개의 주문이 들어와도 재고가 정확하게 차감되어야 한다")
        fun `concurrency test for order creation`() {
            // Given: 재고가 100개인 상품 생성
            val initialStock = 100
            val productId = createProduct(
                mockMvc = mockMvc,
                objectMapper = objectMapper,
                stockQty = initialStock
            )

            val threadCount = 100
            val executorService = Executors.newFixedThreadPool(32)
            val latch = CountDownLatch(threadCount)
            val successCount = AtomicInteger(0)
            val failCount = AtomicInteger(0)

            // When: 100개의 쓰레드에서 동시에 각각 1개씩 주문
            for (i in 1..threadCount) {
                executorService.submit {
                    try {
                        val request = createOrderRequest(
                            orderLines = listOf(
                                createOrderLineRequest(productId = productId, qty = 1)
                            )
                        )
                        createOrder(mockMvc, objectMapper, request)
                        successCount.incrementAndGet()
                    } catch (e: Exception) {
                        failCount.incrementAndGet()
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()
            executorService.shutdown()

            // Then: 모든 주문이 성공하고 재고가 0이어야 함
            assertThat(successCount.get()).isEqualTo(threadCount)
            assertThat(failCount.get()).isEqualTo(0)

            val productResult = mockMvc.perform(get("/api/v1/products/{productId}", productId))
                .andExpect(status().isOk)
                .andReturn()

            val stockQty = objectMapper.readTree(productResult.response.contentAsString)
                .get("stockQty")
                .asInt()

            assertThat(stockQty).isEqualTo(0)
        }
    }
}

package com.spoqa.hiringchallenge.interfaces.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.spoqa.hiringchallenge.interfaces.product.fixture.ProductRequestFixtures.createProductRequest
import com.spoqa.hiringchallenge.interfaces.product.fixture.ProductRequestFixtures.updateProductRequest
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

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("ProductController E2E")
class ProductE2ETest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @Nested
    @DisplayName("POST /api/v1/products")
    inner class CreateProduct {
        @Test
        fun `상품을 생성한다`() {
            mockMvc.perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProductRequest())),
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.productId").exists())
                .andExpect(jsonPath("$.productName").value("콜라"))
                .andExpect(jsonPath("$.unit").value("캔"))
                .andExpect(jsonPath("$.unitPrice").value(1500))
                .andExpect(jsonPath("$.stockQty").value(20))
        }

        @Test
        fun `상품명이 비어 있으면 400을 응답한다`() {
            mockMvc.perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProductRequest(productName = "   "))),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품명은 비어 있을 수 없습니다."))
        }

        @Test
        fun `단위가 비어 있으면 400을 응답한다`() {
            mockMvc.perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProductRequest(unit = "   "))),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("단위는 비어 있을 수 없습니다."))
        }

        @Test
        fun `단가가 음수이면 400을 응답한다`() {
            mockMvc.perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProductRequest(unitPrice = -1))),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("단가는 1원 이상 1000000000원 이하여야 합니다."))
        }

        @Test
        fun `재고 수량이 음수이면 400을 응답한다`() {
            mockMvc.perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProductRequest(stockQty = -1))),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("재고 수량은 0개 이상 1000000개 이하여야 합니다."))
        }

        @Test
        fun `요청 본문이 JSON 형식이 아니면 400을 응답한다`() {
            mockMvc.perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("invalid-json"),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("요청 본문을 읽을 수 없습니다."))
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products/{productId}")
    inner class FindProduct {
        @Test
        fun `상품을 단건 조회한다`() {
            val createResult =
                mockMvc.perform(
                    post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            objectMapper.writeValueAsString(
                                createProductRequest(
                                    productName = "사이다",
                                    unit = "병",
                                    unitPrice = 1_800,
                                    stockQty = 12,
                                ),
                            ),
                        ),
                )
                    .andExpect(status().isCreated)
                    .andReturn()

            val productId = objectMapper.readTree(createResult.response.contentAsString)
                .get("productId")
                .asText()

            mockMvc.perform(get("/api/v1/products/{productId}", productId))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.productName").value("사이다"))
                .andExpect(jsonPath("$.unit").value("병"))
                .andExpect(jsonPath("$.unitPrice").value(1800))
                .andExpect(jsonPath("$.stockQty").value(12))
        }

        @Test
        fun `존재하지 않는 상품이면 404를 응답한다`() {
            val productId = UUID.randomUUID()

            mockMvc.perform(get("/api/v1/products/{productId}", productId))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다. productId=$productId"))
        }

        @Test
        fun `상품 ID가 UUID 형식이 아니면 400을 응답한다`() {
            mockMvc.perform(get("/api/v1/products/{productId}", "invalid-product-id"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 값입니다. name=productId"))
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products")
    inner class FindProducts {
        @Test
        fun `상품 목록을 조회한다`() {
            mockMvc.perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            createProductRequest(
                                productName = "오렌지주스",
                                unit = "팩",
                                unitPrice = 2_500,
                                stockQty = 30,
                            ),
                        ),
                    ),
            )
                .andExpect(status().isCreated)

            mockMvc.perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            createProductRequest(
                                productName = "생수",
                                unit = "병",
                                unitPrice = 900,
                                stockQty = 100,
                            ),
                        ),
                    ),
            )
                .andExpect(status().isCreated)

            mockMvc.perform(get("/api/v1/products?page=0&size=100"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.content[*].productName", hasItems("오렌지주스", "생수")))
                .andExpect(jsonPath("$.content[*].unit", hasItems("팩", "병")))
                .andExpect(jsonPath("$.content[*].unitPrice", hasItems(2500, 900)))
                .andExpect(jsonPath("$.content[*].stockQty", hasItems(30, 100)))
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(100))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").exists())
        }

        @Test
        fun `page 파라미터가 없으면 400을 응답한다`() {
            mockMvc.perform(get("/api/v1/products?size=10"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("필수 요청 파라미터가 없습니다. name=page"))
        }

        @Test
        fun `page가 음수이면 400을 응답한다`() {
            mockMvc.perform(get("/api/v1/products?page=-1&size=10"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Page index must not be less than zero"))
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/products/{productId}")
    inner class UpdateProduct {
        @Test
        fun `상품을 수정한다`() {
            val createResult =
                mockMvc.perform(
                    post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            objectMapper.writeValueAsString(
                                createProductRequest(
                                    productName = "감자칩",
                                    unit = "봉",
                                    unitPrice = 1_700,
                                    stockQty = 15,
                                ),
                            ),
                        ),
                )
                    .andExpect(status().isCreated)
                    .andReturn()

            val productId = objectMapper.readTree(createResult.response.contentAsString)
                .get("productId")
                .asText()

            mockMvc.perform(
                put("/api/v1/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            updateProductRequest(
                                productName = "감자칩 대용량",
                                unit = "박스",
                                unitPrice = 12_000,
                                stockQty = 5,
                            ),
                        ),
                    ),
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.productName").value("감자칩 대용량"))
                .andExpect(jsonPath("$.unit").value("박스"))
                .andExpect(jsonPath("$.unitPrice").value(12000))
                .andExpect(jsonPath("$.stockQty").value(5))
        }

        @Test
        fun `존재하지 않는 상품을 수정하면 404를 응답한다`() {
            val productId = UUID.randomUUID()

            mockMvc.perform(
                put("/api/v1/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProductRequest())),
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다. productId=$productId"))
        }

        @Test
        fun `수정 요청의 상품명이 비어 있으면 400을 응답한다`() {
            val productId = UUID.randomUUID()

            mockMvc.perform(
                put("/api/v1/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProductRequest(productName = "   "))),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품명은 비어 있을 수 없습니다."))
        }

        @Test
        fun `수정 요청의 단위가 비어 있으면 400을 응답한다`() {
            val productId = UUID.randomUUID()

            mockMvc.perform(
                put("/api/v1/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProductRequest(unit = "   "))),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("단위는 비어 있을 수 없습니다."))
        }

        @Test
        fun `수정 요청의 단가가 음수이면 400을 응답한다`() {
            val productId = UUID.randomUUID()

            mockMvc.perform(
                put("/api/v1/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProductRequest(unitPrice = -1))),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("단가는 1원 이상 1000000000원 이하여야 합니다."))
        }

        @Test
        fun `수정 요청의 재고 수량이 음수이면 400을 응답한다`() {
            val productId = UUID.randomUUID()

            mockMvc.perform(
                put("/api/v1/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProductRequest(stockQty = -1))),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("재고 수량은 0개 이상 1000000개 이하여야 합니다."))
        }

        @Test
        fun `상품 ID가 UUID 형식이 아니면 400을 응답한다`() {
            mockMvc.perform(
                put("/api/v1/products/{productId}", "invalid-product-id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProductRequest())),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 값입니다. name=productId"))
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/products/{productId}")
    inner class DeleteProduct {
        @Test
        fun `상품을 삭제한다`() {
            val createResult =
                mockMvc.perform(
                    post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            objectMapper.writeValueAsString(
                                createProductRequest(
                                    productName = "초콜릿",
                                    unit = "개",
                                    unitPrice = 1_000,
                                    stockQty = 40,
                                ),
                            ),
                        ),
                )
                    .andExpect(status().isCreated)
                    .andReturn()

            val productId = objectMapper.readTree(createResult.response.contentAsString)
                .get("productId")
                .asText()

            mockMvc.perform(delete("/api/v1/products/{productId}", productId))
                .andExpect(status().isNoContent)

            mockMvc.perform(get("/api/v1/products/{productId}", productId))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
        }

        @Test
        fun `존재하지 않는 상품을 삭제하면 404를 응답한다`() {
            val productId = UUID.randomUUID()

            mockMvc.perform(delete("/api/v1/products/{productId}", productId))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다. productId=$productId"))
        }

        @Test
        fun `상품 ID가 UUID 형식이 아니면 400을 응답한다`() {
            mockMvc.perform(delete("/api/v1/products/{productId}", "invalid-product-id"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 값입니다. name=productId"))
        }
    }
}

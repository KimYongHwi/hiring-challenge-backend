package com.spoqa.hiringchallenge.domain.product

import com.spoqa.hiringchallenge.domain.product.vo.ProductId
import com.spoqa.hiringchallenge.domain.product.vo.ProductName
import com.spoqa.hiringchallenge.domain.product.vo.ProductUnit
import com.spoqa.hiringchallenge.domain.product.vo.StockQuantity
import com.spoqa.hiringchallenge.domain.product.vo.UnitPrice
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class ProductTest {

    @Nested
    inner class ProductNameTest {

        @Test
        fun `상품명은 비어 있을 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                ProductName(" ")
            }
        }

        @Test
        fun `상품명은 앞뒤 공백을 포함할 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                ProductName(" 사과")
            }

            assertThrows(IllegalArgumentException::class.java) {
                ProductName("사과 ")
            }
        }

        @Test
        fun `상품명은 최대 100자까지 허용한다`() {
            val validName = "가".repeat(ProductName.MAX_LENGTH)
            val invalidName = "가".repeat(ProductName.MAX_LENGTH + 1)

            assertEquals(validName, ProductName(validName).value)
            assertThrows(IllegalArgumentException::class.java) {
                ProductName(invalidName)
            }
        }
    }

    @Nested
    inner class ProductUnitTest {

        @Test
        fun `단위는 비어 있을 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                ProductUnit("")
            }
        }

        @Test
        fun `단위는 앞뒤 공백을 포함할 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                ProductUnit(" 개")
            }

            assertThrows(IllegalArgumentException::class.java) {
                ProductUnit("개 ")
            }
        }

        @Test
        fun `단위는 최대 30자까지 허용한다`() {
            val validUnit = "개".repeat(ProductUnit.MAX_LENGTH)
            val invalidUnit = "개".repeat(ProductUnit.MAX_LENGTH + 1)

            assertEquals(validUnit, ProductUnit(validUnit).value)
            assertThrows(IllegalArgumentException::class.java) {
                ProductUnit(invalidUnit)
            }
        }
    }

    @Nested
    inner class UnitPriceTest {

        @Test
        fun `단가는 1원 이상이어야 한다`() {
            assertEquals(UnitPrice.MIN, UnitPrice(UnitPrice.MIN).value)
            assertThrows(IllegalArgumentException::class.java) {
                UnitPrice(UnitPrice.MIN - 1)
            }
        }

        @Test
        fun `단가는 상한 이하이어야 한다`() {
            assertEquals(UnitPrice.MAX, UnitPrice(UnitPrice.MAX).value)
            assertThrows(IllegalArgumentException::class.java) {
                UnitPrice(UnitPrice.MAX + 1)
            }
        }
    }

    @Nested
    inner class StockQuantityTest {

        @Test
        fun `재고 수량은 0개 이상이어야 한다`() {
            assertEquals(StockQuantity.MIN, StockQuantity(StockQuantity.MIN).value)
            assertThrows(IllegalArgumentException::class.java) {
                StockQuantity(StockQuantity.MIN - 1)
            }
        }

        @Test
        fun `재고 수량은 상한 이하이어야 한다`() {
            assertEquals(StockQuantity.MAX, StockQuantity(StockQuantity.MAX).value)
            assertThrows(IllegalArgumentException::class.java) {
                StockQuantity(StockQuantity.MAX + 1)
            }
        }
    }

    @Nested
    inner class UpdateTest {

        @Test
        fun `상품 수정은 식별자를 유지하고 수정 가능한 필드만 교체한다`() {
            val productId = ProductId(UUID.randomUUID())
            val product = Product(
                productId = productId,
                productName = ProductName("사과"),
                unit = ProductUnit("개"),
                unitPrice = UnitPrice(1_000),
                stockQty = StockQuantity(10),
            )

            val updated = product.update(
                productName = ProductName("배"),
                unit = ProductUnit("박스"),
                unitPrice = UnitPrice(20_000),
                stockQty = StockQuantity(3),
            )

            assertEquals(productId, updated.productId)
            assertEquals(ProductName("배"), updated.productName)
            assertEquals(ProductUnit("박스"), updated.unit)
            assertEquals(UnitPrice(20_000), updated.unitPrice)
            assertEquals(StockQuantity(3), updated.stockQty)
        }
    }
}

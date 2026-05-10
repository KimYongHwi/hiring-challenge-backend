package com.spoqa.hiringchallenge.domain.order

import com.spoqa.hiringchallenge.domain.order.fixture.OrderFixtures.order
import com.spoqa.hiringchallenge.domain.order.fixture.OrderFixtures.orderLine
import com.spoqa.hiringchallenge.domain.order.vo.OrderAddress
import com.spoqa.hiringchallenge.domain.order.vo.OrderId
import com.spoqa.hiringchallenge.domain.order.vo.OrderQuantity
import com.spoqa.hiringchallenge.domain.order.vo.OrdererName
import com.spoqa.hiringchallenge.domain.order.vo.PhoneNo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class OrderTest {

    @Nested
    inner class OrdererNameTest {

        @Test
        fun `주문자명은 비어 있을 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                OrdererName(" ")
            }
        }

        @Test
        fun `주문자명은 앞뒤 공백을 포함할 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                OrdererName(" 홍길동")
            }

            assertThrows(IllegalArgumentException::class.java) {
                OrdererName("홍길동 ")
            }
        }

        @Test
        fun `주문자명은 최대 50자까지 허용한다`() {
            val validName = "가".repeat(OrdererName.MAX_LENGTH)
            val invalidName = "가".repeat(OrdererName.MAX_LENGTH + 1)

            assertEquals(validName, OrdererName(validName).value)
            assertThrows(IllegalArgumentException::class.java) {
                OrdererName(invalidName)
            }
        }
    }

    @Nested
    inner class OrderAddressTest {

        @Test
        fun `주소는 비어 있을 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                OrderAddress("")
            }
        }

        @Test
        fun `주소는 앞뒤 공백을 포함할 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                OrderAddress(" 서울시")
            }

            assertThrows(IllegalArgumentException::class.java) {
                OrderAddress("서울시 ")
            }
        }

        @Test
        fun `주소는 최대 255자까지 허용한다`() {
            val validAddress = "가".repeat(OrderAddress.MAX_LENGTH)
            val invalidAddress = "가".repeat(OrderAddress.MAX_LENGTH + 1)

            assertEquals(validAddress, OrderAddress(validAddress).value)
            assertThrows(IllegalArgumentException::class.java) {
                OrderAddress(invalidAddress)
            }
        }
    }

    @Nested
    inner class PhoneNoTest {

        @Test
        fun `전화번호는 비어 있을 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                PhoneNo(" ")
            }
        }

        @Test
        fun `전화번호는 앞뒤 공백을 포함할 수 없다`() {
            assertThrows(IllegalArgumentException::class.java) {
                PhoneNo(" 010-1234-5678")
            }

            assertThrows(IllegalArgumentException::class.java) {
                PhoneNo("010-1234-5678 ")
            }
        }

        @Test
        fun `전화번호는 숫자와 하이픈만 포함할 수 있다`() {
            assertEquals("010-1234-5678", PhoneNo("010-1234-5678").value)
            assertThrows(IllegalArgumentException::class.java) {
                PhoneNo("010 1234 5678")
            }
        }

        @Test
        fun `전화번호는 7자 이상 20자 이하이어야 한다`() {
            assertEquals("1234567", PhoneNo("1234567").value)
            assertEquals("1".repeat(PhoneNo.MAX_LENGTH), PhoneNo("1".repeat(PhoneNo.MAX_LENGTH)).value)

            assertThrows(IllegalArgumentException::class.java) {
                PhoneNo("1".repeat(PhoneNo.MIN_LENGTH - 1))
            }
            assertThrows(IllegalArgumentException::class.java) {
                PhoneNo("1".repeat(PhoneNo.MAX_LENGTH + 1))
            }
        }
    }

    @Nested
    inner class OrderQuantityTest {

        @Test
        fun `주문 수량은 1개 이상이어야 한다`() {
            assertEquals(OrderQuantity.MIN, OrderQuantity(OrderQuantity.MIN).value)
            assertThrows(IllegalArgumentException::class.java) {
                OrderQuantity(OrderQuantity.MIN - 1)
            }
        }

        @Test
        fun `주문 수량은 상한 이하이어야 한다`() {
            assertEquals(OrderQuantity.MAX, OrderQuantity(OrderQuantity.MAX).value)
            assertThrows(IllegalArgumentException::class.java) {
                OrderQuantity(OrderQuantity.MAX + 1)
            }
        }
    }

    @Nested
    inner class OrderLineTest {

        @Test
        fun `주문 줄 금액은 주문 시점 단가와 수량으로 계산한다`() {
            val orderLine = orderLine(unitPrice = 3_000, qty = 4)

            assertEquals(12_000, orderLine.totalAmount())
        }
    }

    @Nested
    inner class OrderAggregateTest {

        @Test
        fun `주문 줄은 1개 이상이어야 한다`() {
            assertThrows(IllegalArgumentException::class.java) {
                order(orderLines = emptyList())
            }
        }

        @Test
        fun `주문 줄 개수는 주문 줄 목록 크기이다`() {
            val order = order(
                orderLines = listOf(
                    orderLine(unitPrice = 1_000, qty = 1),
                    orderLine(unitPrice = 2_000, qty = 2),
                ),
            )

            assertEquals(2, order.lineItemCount())
        }

        @Test
        fun `주문 전체 금액은 각 주문 줄 금액의 합이다`() {
            val order = order(
                orderLines = listOf(
                    orderLine(unitPrice = 1_000, qty = 1),
                    orderLine(unitPrice = 2_000, qty = 3),
                ),
            )

            assertEquals(7_000, order.totalAmount())
        }

        @Test
        fun `주문 수정은 식별자를 유지하고 주문 줄은 새 목록으로 대체한다`() {
            val orderId = OrderId(UUID.randomUUID())
            val order = order(
                orderId = orderId,
                orderLines = listOf(orderLine(unitPrice = 1_000, qty = 1)),
            )

            val replacementLines = listOf(
                orderLine(unitPrice = 2_000, qty = 2),
                orderLine(unitPrice = 3_000, qty = 1),
            )
            val updated = order.update(
                ordererName = OrdererName("김철수"),
                address = OrderAddress("서울시 강남구"),
                phoneNo = PhoneNo("010-1111-2222"),
                orderLines = replacementLines,
            )

            assertEquals(orderId, updated.orderId)
            assertEquals(OrdererName("김철수"), updated.ordererName)
            assertEquals(OrderAddress("서울시 강남구"), updated.address)
            assertEquals(PhoneNo("010-1111-2222"), updated.phoneNo)
            assertEquals(replacementLines, updated.orderLines)
            assertEquals(2, updated.lineItemCount())
            assertEquals(7_000, updated.totalAmount())
        }
    }
}

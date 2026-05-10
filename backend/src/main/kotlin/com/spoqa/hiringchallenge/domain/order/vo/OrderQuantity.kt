package com.spoqa.hiringchallenge.domain.order.vo

@JvmInline
value class OrderQuantity(val value: Int) {
    init {
        require(value in MIN..MAX) {
            "주문 수량은 ${MIN}개 이상 ${MAX}개 이하여야 합니다."
        }
    }

    companion object {
        const val MIN: Int = 1
        const val MAX: Int = 1_000_000
    }
}

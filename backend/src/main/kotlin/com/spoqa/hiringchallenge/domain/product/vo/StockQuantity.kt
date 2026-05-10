package com.spoqa.hiringchallenge.domain.product.vo

@JvmInline
value class StockQuantity(val value: Int) {
    init {
        require(value in MIN..MAX) {
            "재고 수량은 ${MIN}개 이상 ${MAX}개 이하여야 합니다."
        }
    }

    companion object {
        const val MIN: Int = 0
        const val MAX: Int = 1_000_000
    }
}

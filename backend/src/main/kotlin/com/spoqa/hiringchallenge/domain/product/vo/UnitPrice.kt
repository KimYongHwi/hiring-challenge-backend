package com.spoqa.hiringchallenge.domain.product.vo

@JvmInline
value class UnitPrice(val value: Long) {
    init {
        require(value in MIN..MAX) {
            "단가는 ${MIN}원 이상 ${MAX}원 이하여야 합니다."
        }
    }

    companion object {
        const val MIN: Long = 1
        const val MAX: Long = 1_000_000_000
    }
}

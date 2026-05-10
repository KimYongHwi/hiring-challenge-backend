package com.spoqa.hiringchallenge.domain.order.vo

@JvmInline
value class OrdererName(val value: String) {
    init {
        require(value.isNotBlank()) { "주문자명은 비어 있을 수 없습니다." }
        require(value == value.trim()) { "주문자명은 앞뒤 공백을 포함할 수 없습니다." }
        require(value.length in MIN_LENGTH..MAX_LENGTH) {
            "주문자명은 ${MIN_LENGTH}자 이상 ${MAX_LENGTH}자 이하여야 합니다."
        }
    }

    companion object {
        const val MIN_LENGTH: Int = 1
        const val MAX_LENGTH: Int = 50
    }
}

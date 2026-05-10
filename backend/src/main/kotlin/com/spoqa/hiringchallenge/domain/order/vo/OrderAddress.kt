package com.spoqa.hiringchallenge.domain.order.vo

@JvmInline
value class OrderAddress(val value: String) {
    init {
        require(value.isNotBlank()) { "주소는 비어 있을 수 없습니다." }
        require(value == value.trim()) { "주소는 앞뒤 공백을 포함할 수 없습니다." }
        require(value.length in MIN_LENGTH..MAX_LENGTH) {
            "주소는 ${MIN_LENGTH}자 이상 ${MAX_LENGTH}자 이하여야 합니다."
        }
    }

    companion object {
        const val MIN_LENGTH: Int = 1
        const val MAX_LENGTH: Int = 255
    }
}

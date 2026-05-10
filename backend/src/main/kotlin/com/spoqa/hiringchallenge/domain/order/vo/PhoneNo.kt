package com.spoqa.hiringchallenge.domain.order.vo

@JvmInline
value class PhoneNo(val value: String) {
    init {
        require(value.isNotBlank()) { "전화번호는 비어 있을 수 없습니다." }
        require(value == value.trim()) { "전화번호는 앞뒤 공백을 포함할 수 없습니다." }
        require(value.length in MIN_LENGTH..MAX_LENGTH) {
            "전화번호는 ${MIN_LENGTH}자 이상 ${MAX_LENGTH}자 이하여야 합니다."
        }
        require(value.all { it.isDigit() || it == '-' }) {
            "전화번호는 숫자와 하이픈만 포함할 수 있습니다."
        }
    }

    companion object {
        const val MIN_LENGTH: Int = 7
        const val MAX_LENGTH: Int = 20
    }
}

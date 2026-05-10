package com.spoqa.hiringchallenge.domain.product.vo

@JvmInline
value class ProductName(val value: String) {
    init {
        require(value.isNotBlank()) { "상품명은 비어 있을 수 없습니다." }
        require(value == value.trim()) { "상품명은 앞뒤 공백을 포함할 수 없습니다." }
        require(value.length in MIN_LENGTH..MAX_LENGTH) {
            "상품명은 ${MIN_LENGTH}자 이상 ${MAX_LENGTH}자 이하여야 합니다."
        }
    }

    companion object {
        const val MIN_LENGTH: Int = 1
        const val MAX_LENGTH: Int = 100
    }
}

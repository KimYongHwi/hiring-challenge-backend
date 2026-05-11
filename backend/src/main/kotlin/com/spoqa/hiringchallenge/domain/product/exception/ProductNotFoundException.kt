package com.spoqa.hiringchallenge.domain.product.exception

import com.spoqa.hiringchallenge.domain.product.vo.ProductId

class ProductNotFoundException(
    productId: ProductId,
) : RuntimeException("상품을 찾을 수 없습니다. productId=${productId.value}")

package com.spoqa.hiringchallenge.domain.product.exception

import com.spoqa.hiringchallenge.domain.exception.NotFoundException
import com.spoqa.hiringchallenge.domain.product.vo.ProductId

class ProductNotFoundException(
    productId: ProductId,
) : NotFoundException(
    code = "PRODUCT_NOT_FOUND",
    message = "상품을 찾을 수 없습니다. productId=${productId.value}",
)

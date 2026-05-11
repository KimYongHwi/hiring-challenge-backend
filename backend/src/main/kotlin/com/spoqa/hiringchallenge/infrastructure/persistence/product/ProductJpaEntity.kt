package com.spoqa.hiringchallenge.infrastructure.persistence.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "products")
class ProductJpaEntity(
    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    val productId: UUID,

    @Column(name = "product_name", nullable = false, length = 100)
    val productName: String,

    @Column(name = "unit", nullable = false, length = 30)
    val unit: String,

    @Column(name = "unit_price", nullable = false)
    val unitPrice: Long,

    @Column(name = "stock_qty", nullable = false)
    val stockQty: Int,
)

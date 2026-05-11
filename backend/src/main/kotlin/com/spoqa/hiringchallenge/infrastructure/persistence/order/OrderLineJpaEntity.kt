package com.spoqa.hiringchallenge.infrastructure.persistence.order

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "order_lines")
class OrderLineJpaEntity(
    @Id
    @Column(name = "order_line_id", nullable = false, updatable = false)
    val orderLineId: UUID,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    val order: OrderJpaEntity,

    @Column(name = "product_id", nullable = false)
    val productId: UUID,

    @Column(name = "qty", nullable = false)
    val qty: Int,

    @Column(name = "unit_price", nullable = false)
    val unitPrice: Long,
)

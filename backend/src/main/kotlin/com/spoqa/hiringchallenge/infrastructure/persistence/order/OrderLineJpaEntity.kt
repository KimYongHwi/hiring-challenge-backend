package com.spoqa.hiringchallenge.infrastructure.persistence.order

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "order_lines")
@SQLDelete(sql = "UPDATE order_lines SET deleted_at = CURRENT_TIMESTAMP WHERE order_line_id = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    val deletedAt: Instant? = null,
)

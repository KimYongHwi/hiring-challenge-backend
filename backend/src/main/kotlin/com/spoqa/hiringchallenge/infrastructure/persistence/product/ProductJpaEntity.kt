package com.spoqa.hiringchallenge.infrastructure.persistence.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted_at = CURRENT_TIMESTAMP WHERE product_id = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    val deletedAt: Instant? = null,
)

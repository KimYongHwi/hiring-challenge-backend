package com.spoqa.hiringchallenge.infrastructure.persistence.order

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = CURRENT_TIMESTAMP WHERE order_id = ?")
@SQLRestriction("deleted_at IS NULL")
class OrderJpaEntity(
    @Id
    @Column(name = "order_id", nullable = false, updatable = false)
    val orderId: UUID,

    @Column(name = "orderer_name", nullable = false, length = 50)
    val ordererName: String,

    @Column(name = "address", nullable = false, length = 255)
    val address: String,

    @Column(name = "phone_no", nullable = false, length = 20)
    val phoneNo: String,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    val deletedAt: Instant? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val orderLines: MutableList<OrderLineJpaEntity> = mutableListOf(),
)

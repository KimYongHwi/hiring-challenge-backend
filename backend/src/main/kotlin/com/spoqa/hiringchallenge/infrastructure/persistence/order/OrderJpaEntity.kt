package com.spoqa.hiringchallenge.infrastructure.persistence.order

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "orders")
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

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val orderLines: MutableList<OrderLineJpaEntity> = mutableListOf(),
)

package com.spoqa.hiringchallenge.infrastructure.persistence.order

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderLineJpaRepository : JpaRepository<OrderLineJpaEntity, UUID>

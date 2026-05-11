package com.spoqa.hiringchallenge.infrastructure.persistence.order

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, UUID> {
    @EntityGraph(attributePaths = ["orderLines"])
    override fun findById(id: UUID): Optional<OrderJpaEntity>

    @EntityGraph(attributePaths = ["orderLines"])
    override fun findAll(pageable: Pageable): Page<OrderJpaEntity>
}

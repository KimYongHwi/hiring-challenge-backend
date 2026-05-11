package com.spoqa.hiringchallenge.infrastructure.persistence.product

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface ProductJpaRepository : JpaRepository<ProductJpaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProductJpaEntity p where p.productId = :productId")
    fun findByIdForUpdate(productId: UUID): ProductJpaEntity?
}

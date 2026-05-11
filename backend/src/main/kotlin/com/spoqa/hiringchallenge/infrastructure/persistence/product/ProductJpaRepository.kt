package com.spoqa.hiringchallenge.infrastructure.persistence.product

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductJpaRepository : JpaRepository<ProductJpaEntity, UUID>

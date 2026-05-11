package com.spoqa.hiringchallenge.domain.exception

abstract class BusinessException(
    val code: String,
    override val message: String,
) : RuntimeException(message)

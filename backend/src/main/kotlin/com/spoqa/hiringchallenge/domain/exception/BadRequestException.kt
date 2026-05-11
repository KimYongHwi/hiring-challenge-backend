package com.spoqa.hiringchallenge.domain.exception

open class BadRequestException(
    code: String,
    message: String,
) : BusinessException(code, message)

package com.spoqa.hiringchallenge.domain.exception

abstract class NotFoundException(
    code: String,
    message: String,
) : BusinessException(code, message)

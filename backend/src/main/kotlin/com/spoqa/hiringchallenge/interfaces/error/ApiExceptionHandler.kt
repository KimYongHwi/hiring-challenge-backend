package com.spoqa.hiringchallenge.interfaces.error

import com.spoqa.hiringchallenge.domain.exception.BadRequestException
import com.spoqa.hiringchallenge.domain.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.bind.MethodArgumentNotValidException

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ApiErrorResponse(
                    code = exception.code,
                    message = exception.message,
                ),
            )

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(exception: BadRequestException): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.badRequest()
            .body(
                ApiErrorResponse(
                    code = exception.code,
                    message = exception.message,
                ),
            )

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.badRequest()
            .body(
                ApiErrorResponse(
                    code = "BAD_REQUEST",
                    message = exception.message ?: "잘못된 요청입니다.",
                ),
            )

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        exception: MethodArgumentTypeMismatchException,
    ): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.badRequest()
            .body(
                ApiErrorResponse(
                    code = "BAD_REQUEST",
                    message = "잘못된 요청 값입니다. name=${exception.name}",
                ),
            )

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(
        exception: MissingServletRequestParameterException,
    ): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.badRequest()
            .body(
                ApiErrorResponse(
                    code = "BAD_REQUEST",
                    message = "필수 요청 파라미터가 없습니다. name=${exception.parameterName}",
                ),
            )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        exception: HttpMessageNotReadableException,
    ): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.badRequest()
            .body(
                ApiErrorResponse(
                    code = "BAD_REQUEST",
                    message = "요청 본문을 읽을 수 없습니다.",
                ),
            )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
    ): ResponseEntity<ApiErrorResponse> {
        val message =
            exception.bindingResult
                .allErrors
                .firstOrNull()
                ?.let { error ->
                    when (error) {
                        is FieldError -> error.defaultMessage ?: "요청 값이 올바르지 않습니다."
                        else -> error.defaultMessage ?: "요청 값이 올바르지 않습니다."
                    }
                }
                ?: "요청 값이 올바르지 않습니다."

        return ResponseEntity.badRequest()
            .body(
                ApiErrorResponse(
                    code = "BAD_REQUEST",
                    message = message,
                ),
            )
    }
}

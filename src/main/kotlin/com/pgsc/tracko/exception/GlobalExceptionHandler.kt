package com.pgsc.tracko.exception

import com.pgsc.tracko.dto.ApiResponse
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<ApiResponse<Unit>> {
        log.debug("Resource not found: {}", ex.message)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.failure("NOT_FOUND", ex.message ?: "Resource not found"))
    }

    @ExceptionHandler(InvalidStatusTransitionException::class)
    fun handleInvalidStatusTransition(ex: InvalidStatusTransitionException): ResponseEntity<ApiResponse<Unit>> {
        log.debug("Invalid status transition: {}", ex.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure("INVALID_STATUS_TRANSITION", ex.message ?: "Invalid status transition"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        val details = ex.bindingResult.fieldErrors.associate { error ->
            error.field to (error.defaultMessage ?: "Invalid value")
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiResponse.failure(
                    code = "VALIDATION_FAILED",
                    message = "Validation failed",
                    details = details
                )
            )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<ApiResponse<Unit>> {
        val details = ex.constraintViolations
            .groupBy { it.propertyPath.toString() }
            .mapValues { (_, violations) -> violations.map { it.message }.let { msgs -> if (msgs.size == 1) msgs.single() else msgs } }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiResponse.failure(
                    code = "CONSTRAINT_VIOLATION",
                    message = "Constraint violation",
                    details = details
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error("Unexpected error", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse.failure(
                    code = "INTERNAL_ERROR",
                    message = "An unexpected error occurred"
                )
            )
    }
}

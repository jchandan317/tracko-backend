package com.pgsc.tracko.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant

/**
 * Standard API response wrapper for all endpoints.
 * Success: data populated, error null.
 * Failure: data null, error populated.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val timestamp: Instant = Instant.now(),
    val success: Boolean,
    val data: T? = null,
    val error: ApiErrorPayload? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> =
            ApiResponse(success = true, data = data, error = null)

        fun <T> failure(code: String, message: String, details: Map<String, Any?>? = null): ApiResponse<T> =
            ApiResponse(
                success = false,
                data = null,
                error = ApiErrorPayload(code = code, message = message, details = details)
            )
    }
}

data class ApiErrorPayload(
    val code: String,
    val message: String,
    val details: Map<String, Any?>? = null
)

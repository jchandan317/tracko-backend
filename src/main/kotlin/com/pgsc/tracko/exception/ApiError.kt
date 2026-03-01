package com.pgsc.tracko.exception

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Instant

data class ApiError(
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val message: String,
    val errors: Map<String, List<String>> = emptyMap()
)

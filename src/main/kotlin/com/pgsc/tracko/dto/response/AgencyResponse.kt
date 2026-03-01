package com.pgsc.tracko.dto.response

import java.time.Instant

data class AgencyResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String?,
    val createdAt: Instant?
)

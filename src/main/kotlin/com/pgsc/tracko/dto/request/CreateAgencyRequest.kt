package com.pgsc.tracko.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateAgencyRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(max = 255)
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email
    @field:Size(max = 255)
    val email: String,

    @field:Size(max = 50)
    val phone: String? = null
)

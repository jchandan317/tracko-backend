package com.pgsc.tracko.dto.request

import com.pgsc.tracko.domain.ticket.TicketCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateTicketRequest(
    @field:NotBlank(message = "Flat number is required")
    @field:Size(max = 50)
    val flatNumber: String,

    @field:NotNull(message = "Category is required")
    val category: TicketCategory,

    @field:NotBlank(message = "Summary is required")
    @field:Size(max = 1000)
    val summary: String
)

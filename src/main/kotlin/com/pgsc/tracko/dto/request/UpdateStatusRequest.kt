package com.pgsc.tracko.dto.request

import com.pgsc.tracko.domain.ticket.TicketStatus
import jakarta.validation.constraints.NotNull

data class UpdateStatusRequest(
    @field:NotNull(message = "Status is required")
    val status: TicketStatus
)

package com.pgsc.tracko.dto.response

import com.pgsc.tracko.domain.ticket.TicketCategory
import com.pgsc.tracko.domain.ticket.TicketStatus
import java.time.LocalDateTime

data class TicketResponse(
    val id: Long,
    val flatNumber: String,
    val category: TicketCategory,
    val summary: String,
    val status: TicketStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

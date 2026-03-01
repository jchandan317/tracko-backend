package com.pgsc.tracko.service

import com.pgsc.tracko.domain.ticket.Ticket
import com.pgsc.tracko.domain.ticket.TicketCategory
import com.pgsc.tracko.domain.ticket.TicketStatus
import com.pgsc.tracko.dto.PageResponse
import com.pgsc.tracko.dto.request.CreateTicketRequest
import com.pgsc.tracko.dto.response.TicketResponse
import com.pgsc.tracko.exception.InvalidStatusTransitionException
import com.pgsc.tracko.exception.ResourceNotFoundException
import com.pgsc.tracko.repository.TicketRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import jakarta.persistence.criteria.Predicate

@Service
class TicketService(
    private val ticketRepository: TicketRepository
) {

    private val validTransitions = mapOf(
        TicketStatus.OPEN to TicketStatus.IN_PROGRESS,
        TicketStatus.IN_PROGRESS to TicketStatus.RESOLVED,
        TicketStatus.RESOLVED to TicketStatus.CLOSED
    )

    fun create(request: CreateTicketRequest): TicketResponse {
        val ticket = Ticket(
            flatNumber = request.flatNumber,
            category = request.category,
            summary = request.summary,
            status = TicketStatus.OPEN
        )
        val saved = ticketRepository.save(ticket)
        return toResponse(saved)
    }

    fun findById(id: Long): TicketResponse {
        val ticket = ticketRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Ticket not found with id: $id") }
        return toResponse(ticket)
    }

    fun findAll(page: Int, size: Int, status: TicketStatus?, category: TicketCategory?): PageResponse<TicketResponse> {
        val spec = buildSpecification(status, category)
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val result = if (spec != null) {
            ticketRepository.findAll(spec, pageable)
        } else {
            ticketRepository.findAll(pageable)
        }
        return PageResponse(
            content = result.content.map { toResponse(it) },
            page = result.number,
            size = result.size,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
            first = result.isFirst,
            last = result.isLast
        )
    }

    fun updateStatus(id: Long, newStatus: TicketStatus): TicketResponse {
        val ticket = ticketRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Ticket not found with id: $id") }

        validateStatusTransition(ticket.status, newStatus)

        ticket.status = newStatus
        val updated = ticketRepository.save(ticket)
        return toResponse(updated)
    }

    private fun validateStatusTransition(current: TicketStatus, newStatus: TicketStatus) {
        if (current == TicketStatus.CLOSED) {
            throw InvalidStatusTransitionException("Cannot change status: ticket is already CLOSED")
        }
        val allowedNext = validTransitions[current]
        if (allowedNext != newStatus) {
            throw InvalidStatusTransitionException(
                "Invalid transition from $current to $newStatus. " +
                    "Allowed: OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED"
            )
        }
    }

    private fun buildSpecification(status: TicketStatus?, category: TicketCategory?): Specification<Ticket>? {
        if (status == null && category == null) return null
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            status?.let { predicates.add(cb.equal(root.get<TicketStatus>("status"), it)) }
            category?.let { predicates.add(cb.equal(root.get<TicketCategory>("category"), it)) }
            cb.and(*predicates.toTypedArray())
        }
    }

    private fun toResponse(ticket: Ticket): TicketResponse {
        return TicketResponse(
            id = ticket.id!!,
            flatNumber = ticket.flatNumber,
            category = ticket.category,
            summary = ticket.summary,
            status = ticket.status,
            createdAt = ticket.createdAt,
            updatedAt = ticket.updatedAt
        )
    }
}

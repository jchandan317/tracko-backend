package com.pgsc.tracko.service

import com.pgsc.tracko.domain.ticket.Ticket
import com.pgsc.tracko.domain.ticket.TicketCategory
import com.pgsc.tracko.domain.ticket.TicketStatus
import com.pgsc.tracko.dto.PageResponse
import com.pgsc.tracko.dto.request.CreateTicketRequest
import com.pgsc.tracko.dto.response.TicketResponse
import com.pgsc.tracko.exception.InvalidStatusTransitionException
import com.pgsc.tracko.exception.ResourceNotFoundException
import com.pgsc.tracko.repository.AgencyRepository
import com.pgsc.tracko.repository.TicketRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import jakarta.persistence.criteria.Predicate

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val agencyRepository: AgencyRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val validTransitions = mapOf(
        TicketStatus.OPEN to TicketStatus.IN_PROGRESS,
        TicketStatus.IN_PROGRESS to TicketStatus.RESOLVED,
        TicketStatus.RESOLVED to TicketStatus.CLOSED
    )

    @CacheEvict(cacheNames = ["tickets", "ticketList"], allEntries = true)
    fun create(agencyId: Long, request: CreateTicketRequest): TicketResponse {
        val agency = agencyRepository.findById(agencyId)
            .orElseThrow { ResourceNotFoundException("Agency not found with id: $agencyId") }
        val ticket = Ticket(
            agency = agency,
            flatNumber = request.flatNumber,
            category = request.category,
            summary = request.summary,
            status = TicketStatus.OPEN
        )
        val saved = ticketRepository.save(ticket)
        return toResponse(saved)
    }

    @Cacheable(cacheNames = ["tickets"], key = "#agencyId + '-' + #id")
    fun findById(agencyId: Long, id: Long): TicketResponse {
        log.debug("Ticket findById cache miss - querying DB for agencyId={} id={}", agencyId, id)
        val ticket = ticketRepository.findByIdAndAgency_Id(id, agencyId)
            ?: throw ResourceNotFoundException("Ticket not found with id: $id")
        return toResponse(ticket)
    }

    @Cacheable(cacheNames = ["ticketList"], key = "#agencyId + '-' + #page + '-' + #size + '-' + #status + '-' + #category")
    fun findAll(
        agencyId: Long,
        page: Int,
        size: Int,
        status: TicketStatus?,
        category: TicketCategory?
    ): PageResponse<TicketResponse> {
        val spec = buildSpecification(agencyId, status, category)
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val result = ticketRepository.findAll(spec, pageable)
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

    @CacheEvict(cacheNames = ["tickets", "ticketList"], allEntries = true)
    fun updateStatus(agencyId: Long, id: Long, newStatus: TicketStatus): TicketResponse {
        val ticket = ticketRepository.findByIdAndAgency_Id(id, agencyId)
            ?: throw ResourceNotFoundException("Ticket not found with id: $id")

        validateStatusTransition(ticket.status, newStatus)

        ticket.status = newStatus
        val updated = ticketRepository.save(ticket)
        return toResponse(updated)
    }

    @CacheEvict(cacheNames = ["tickets", "ticketList"], allEntries = true)
    fun softDelete(agencyId: Long, id: Long) {
        val updated = ticketRepository.softDeleteByIdAndAgencyId(id, agencyId)
        if (updated == 0) throw ResourceNotFoundException("Ticket not found with id: $id")
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

    private fun buildSpecification(
        agencyId: Long,
        status: TicketStatus?,
        category: TicketCategory?
    ): Specification<Ticket> {
        return Specification { root, _, cb ->
            val agency = root.join<Any, Any>("agency")
            val predicates = mutableListOf<Predicate>(
                cb.equal(agency.get<Long>("id"), agencyId)
            )
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

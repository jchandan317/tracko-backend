package com.pgsc.tracko.repository

import com.pgsc.tracko.domain.ticket.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket>

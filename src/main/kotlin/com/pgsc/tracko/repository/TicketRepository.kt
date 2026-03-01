package com.pgsc.tracko.repository

import com.pgsc.tracko.domain.ticket.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    fun findByIdAndAgency_Id(id: Long, agencyId: Long): Ticket?

    /**
     * Soft delete: set is_deleted = true.
     * Uses a custom query so @Where does not prevent the update.
     */
    @Modifying
    @Query("UPDATE Ticket t SET t.isDeleted = true WHERE t.id = :id AND t.agency.id = :agencyId")
    fun softDeleteByIdAndAgencyId(id: Long, agencyId: Long): Int
}

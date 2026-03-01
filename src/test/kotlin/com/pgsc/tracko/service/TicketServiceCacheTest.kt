package com.pgsc.tracko.service

import com.pgsc.tracko.domain.agency.Agency
import com.pgsc.tracko.domain.ticket.Ticket
import com.pgsc.tracko.domain.ticket.TicketCategory
import com.pgsc.tracko.domain.ticket.TicketStatus
import com.pgsc.tracko.repository.TicketRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime

@SpringBootTest
class TicketServiceCacheTest {

    @Autowired
    private lateinit var ticketService: TicketService

    @MockBean
    private lateinit var ticketRepository: TicketRepository

    @MockBean
    private lateinit var agencyRepository: com.pgsc.tracko.repository.AgencyRepository

    @Test
    fun `findById - second call is served from cache and does not hit DB`() {
        val agency = Agency(id = 1L, name = "Test Agency", email = "a@b.com", phone = null)
        val ticket = Ticket(
            id = 1L,
            agency = agency,
            flatNumber = "F1",
            category = TicketCategory.GENERAL,
            summary = "Summary",
            status = TicketStatus.OPEN,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        whenever(ticketRepository.findByIdAndAgency_Id(eq(1L), eq(1L))).thenReturn(ticket)

        ticketService.findById(1L, 1L)
        ticketService.findById(1L, 1L)

        verify(ticketRepository, times(1)).findByIdAndAgency_Id(1L, 1L)
    }
}

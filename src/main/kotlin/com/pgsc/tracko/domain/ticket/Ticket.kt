package com.pgsc.tracko.domain.ticket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(
    name = "ticket",
    indexes = [
        Index(name = "idx_ticket_status", columnList = "status"),
        Index(name = "idx_ticket_category", columnList = "category"),
        Index(name = "idx_ticket_created_at", columnList = "createdAt")
    ]
)
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var flatNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: TicketCategory,

    @Column(nullable = false, length = 1000)
    var summary: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TicketStatus = TicketStatus.OPEN,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @jakarta.persistence.PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}

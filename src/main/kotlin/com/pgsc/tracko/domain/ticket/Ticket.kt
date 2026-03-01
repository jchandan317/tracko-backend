package com.pgsc.tracko.domain.ticket

import com.pgsc.tracko.domain.agency.Agency
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(
    name = "ticket",
    indexes = [
        Index(name = "idx_ticket_status", columnList = "status"),
        Index(name = "idx_ticket_category", columnList = "category"),
        Index(name = "idx_ticket_created_at", columnList = "created_at"),
        Index(name = "idx_ticket_agency_id", columnList = "agency_id")
    ]
)
@EntityListeners(AuditingEntityListener::class)
@SQLRestriction("is_deleted = false")
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    var agency: Agency,

    @Column(name = "flat_number", nullable = false)
    var flatNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: TicketCategory,

    @Column(nullable = false, length = 1000)
    var summary: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TicketStatus = TicketStatus.OPEN,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: String? = null,

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: String? = null
) {
    @jakarta.persistence.PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}

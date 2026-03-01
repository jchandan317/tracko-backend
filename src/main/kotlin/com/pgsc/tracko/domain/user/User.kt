package com.pgsc.tracko.domain.user

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
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

/**
 * User entity for future JWT-based authentication.
 * No password or auth logic yet - structure only.
 */
@Entity
@Table(name = "app_user")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    var role: Role = Role.WORKER,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    var agency: Agency? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null
)

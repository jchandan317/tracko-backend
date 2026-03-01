package com.pgsc.tracko.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.Optional

/**
 * Provides current auditor for @CreatedBy / @LastModifiedBy.
 * Replace with JWT-based principal once authentication is implemented.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class JpaAuditingConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> = AuditorAware {
        // TODO: return Optional.ofNullable(SecurityContextHolder.getContext().authentication?.name)
        Optional.of("system")
    }
}

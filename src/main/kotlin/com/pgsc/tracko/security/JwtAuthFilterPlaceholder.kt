package com.pgsc.tracko.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Placeholder for future JWT authentication filter.
 * When implementing: validate JWT, set SecurityContext, then chain.doFilter().
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 100)
class JwtAuthFilterPlaceholder : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // TODO: extract Bearer token, validate, set SecurityContextHolder
        filterChain.doFilter(request, response)
    }
}

package com.pgsc.tracko.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Logs request method, path, status and duration.
 * Does not log request/response body to avoid sensitive data.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class RequestLoggingFilter : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!log.isDebugEnabled) {
            filterChain.doFilter(request, response)
            return
        }
        val start = System.currentTimeMillis()
        val path = request.requestURI
        val method = request.method
        try {
            filterChain.doFilter(request, response)
        } finally {
            val duration = System.currentTimeMillis() - start
            log.debug("request_duration_ms={} method={} path={} status={}", duration, method, path, response.status)
        }
    }
}

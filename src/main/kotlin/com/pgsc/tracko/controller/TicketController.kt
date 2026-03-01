package com.pgsc.tracko.controller

import com.pgsc.tracko.domain.ticket.TicketCategory
import com.pgsc.tracko.domain.ticket.TicketStatus
import com.pgsc.tracko.dto.request.CreateTicketRequest
import com.pgsc.tracko.dto.request.UpdateStatusRequest
import com.pgsc.tracko.dto.response.TicketResponse
import com.pgsc.tracko.service.TicketService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import com.pgsc.tracko.dto.PageResponse
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/api/tickets")
class TicketController(
    private val ticketService: TicketService
) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateTicketRequest): ResponseEntity<TicketResponse> {
        val response = ticketService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") @Min(0) page: Int,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) size: Int,
        @RequestParam(required = false) status: TicketStatus?,
        @RequestParam(required = false) category: TicketCategory?
    ): ResponseEntity<PageResponse<TicketResponse>> {
        val response = ticketService.findAll(page, size, status, category)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<TicketResponse> {
        val response = ticketService.findById(id)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateStatusRequest
    ): ResponseEntity<TicketResponse> {
        val response = ticketService.updateStatus(id, request.status)
        return ResponseEntity.ok(response)
    }
}

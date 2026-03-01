package com.pgsc.tracko.controller

import com.pgsc.tracko.domain.ticket.TicketCategory
import com.pgsc.tracko.domain.ticket.TicketStatus
import com.pgsc.tracko.dto.ApiResponse
import com.pgsc.tracko.dto.PageResponse
import com.pgsc.tracko.dto.request.CreateTicketRequest
import com.pgsc.tracko.dto.request.UpdateStatusRequest
import com.pgsc.tracko.dto.response.TicketResponse
import com.pgsc.tracko.service.TicketService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/api/tickets")
class TicketController(
    private val ticketService: TicketService
) {

    @PostMapping
    fun create(
        @RequestParam agencyId: Long,
        @Valid @RequestBody request: CreateTicketRequest
    ): ResponseEntity<ApiResponse<TicketResponse>> {
        val response = ticketService.create(agencyId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response))
    }

    @GetMapping
    fun findAll(
        @RequestParam agencyId: Long,
        @RequestParam(defaultValue = "0") @Min(0) page: Int,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) size: Int,
        @RequestParam(required = false) status: TicketStatus?,
        @RequestParam(required = false) category: TicketCategory?
    ): ResponseEntity<ApiResponse<PageResponse<TicketResponse>>> {
        val response = ticketService.findAll(agencyId, page, size, status, category)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping("/{id}")
    fun findById(
        @RequestParam agencyId: Long,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<TicketResponse>> {
        val response = ticketService.findById(agencyId, id)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PutMapping("/{id}/status")
    fun updateStatus(
        @RequestParam agencyId: Long,
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateStatusRequest
    ): ResponseEntity<ApiResponse<TicketResponse>> {
        val response = ticketService.updateStatus(agencyId, id, request.status)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @RequestParam agencyId: Long,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        ticketService.softDelete(agencyId, id)
        return ResponseEntity.ok(ApiResponse.success(Unit))
    }
}

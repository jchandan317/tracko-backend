package com.pgsc.tracko.controller

import com.pgsc.tracko.dto.ApiResponse
import com.pgsc.tracko.dto.request.CreateAgencyRequest
import com.pgsc.tracko.dto.response.AgencyResponse
import com.pgsc.tracko.service.AgencyService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/agencies")
class AgencyController(
    private val agencyService: AgencyService
) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateAgencyRequest): ResponseEntity<ApiResponse<AgencyResponse>> {
        val response = agencyService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<ApiResponse<AgencyResponse>> {
        val response = agencyService.findById(id)
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}

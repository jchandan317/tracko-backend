package com.pgsc.tracko.service

import com.pgsc.tracko.domain.agency.Agency
import com.pgsc.tracko.dto.request.CreateAgencyRequest
import com.pgsc.tracko.dto.response.AgencyResponse
import com.pgsc.tracko.exception.ResourceNotFoundException
import com.pgsc.tracko.repository.AgencyRepository
import org.springframework.stereotype.Service

@Service
class AgencyService(
    private val agencyRepository: AgencyRepository
) {

    fun create(request: CreateAgencyRequest): AgencyResponse {
        val agency = Agency(
            name = request.name,
            email = request.email,
            phone = request.phone
        )
        val saved = agencyRepository.save(agency)
        return toResponse(saved)
    }

    fun findById(id: Long): AgencyResponse {
        val agency = agencyRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Agency not found with id: $id") }
        return toResponse(agency)
    }

    private fun toResponse(agency: Agency): AgencyResponse = AgencyResponse(
        id = agency.id!!,
        name = agency.name,
        email = agency.email,
        phone = agency.phone,
        createdAt = agency.createdAt
    )
}

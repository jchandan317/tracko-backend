package com.pgsc.tracko.repository

import com.pgsc.tracko.domain.agency.Agency
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AgencyRepository : JpaRepository<Agency, Long>

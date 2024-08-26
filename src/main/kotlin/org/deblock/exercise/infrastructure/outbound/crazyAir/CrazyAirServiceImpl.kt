package org.deblock.exercise.infrastructure.outbound.crazyAir

import org.deblock.exercise.domain.dto.CrazyAirSupplierDto
import org.deblock.exercise.domain.dto.toFlight
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.valueObject.Supplier
import org.deblock.exercise.infrastructure.SupplierService
import org.deblock.exercise.infrastructure.inbound.filter.RequestIdFilter
import org.deblock.exercise.infrastructure.outbound.ApiCaller
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CrazyAirServiceImpl : SupplierService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var apiCaller: ApiCaller<List<CrazyAirSupplierDto>>

    @Autowired
    private lateinit var crazyAirApiService: CrazyAirApiService


    override fun getFlights(
        origin: String,
        destination: String,
        departureDate: String,
        returnDate: String,
        passengerCount: String
    ): List<Flight> {
        val requestId = MDC.get(RequestIdFilter.REQUEST_ID)
        log.info("[$requestId] about to execute external call from ${this.javaClass} Service")
        return try {
            val response = apiCaller.execute {
                crazyAirApiService.getFlights(
                    origin,
                    destination,
                    departureDate,
                    returnDate,
                    passengerCount
                )
            }
            log.info("[$requestId] Call finished ${this.javaClass}")
            response.body()?.map { it.toFlight(Supplier.CRAZY_AIR.name) } ?: emptyList()
        } catch (e: Exception) {
            log.error("[$requestId] Unsuccessful call, exception ${e.message} ${this.javaClass}")
            emptyList()
        }
    }
}
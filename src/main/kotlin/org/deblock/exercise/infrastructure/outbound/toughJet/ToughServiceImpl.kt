package org.deblock.exercise.infrastructure.outbound.toughJet

import org.deblock.exercise.domain.dto.ToughJetSupplierDto
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
class ToughServiceImpl : SupplierService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var apiCaller: ApiCaller<List<ToughJetSupplierDto>>

    @Autowired
    private lateinit var toughJetApiService: ToughJetApiService


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
                toughJetApiService.getFlights(
                    origin,
                    destination,
                    departureDate,
                    returnDate,
                    passengerCount
                )
            }
            log.info("[$requestId] Call finished ${this.javaClass}")
            response.body()?.map { it.toFlight(Supplier.TOUGH_JET.name) } ?: emptyList()
        } catch (e: Exception) {
            log.error("[$requestId] Unsuccessful call, exception ${e.message} ${this.javaClass}")
            emptyList()
        }
    }
}
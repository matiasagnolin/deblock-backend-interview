package org.deblock.exercise.application

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.infrastructure.SupplierService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class FlightSearcherServiceImpl(private val supplierService: List<SupplierService>) : FlightSearcherService {
    override fun findFlights(
        origin: String,
        destination: String,
        departureDate: LocalDate,
        returnDate: LocalDate,
        numberOfPassengers: Int
    ): List<Flight> = runBlocking {
        findFlightsConcurrently(
            origin,
            destination,
            departureDate,
            returnDate,
            numberOfPassengers
        )
    }

    private suspend fun findFlightsConcurrently(
        origin: String,
        destination: String,
        departureDate: LocalDate,
        returnDate: LocalDate,
        numberOfPassengers: Int
    ): List<Flight> = coroutineScope {
        supplierService.map {
            async {
                it.getFlights(
                    origin,
                    destination,
                    departureDate.toString(),
                    returnDate.toString(),
                    numberOfPassengers.toString()
                )
            }
        }.map { it.await() }.flatten().sortedBy { it.fare }
    }
}

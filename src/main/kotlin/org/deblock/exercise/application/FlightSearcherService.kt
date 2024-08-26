package org.deblock.exercise.application

import org.deblock.exercise.domain.model.Flight
import java.time.LocalDate

interface FlightSearcherService {
    fun findFlights(
        origin: String,
        destination: String,
        departureDate: LocalDate,
        returnDate: LocalDate,
        numberOfPassengers: Int
    ): List<Flight>
}
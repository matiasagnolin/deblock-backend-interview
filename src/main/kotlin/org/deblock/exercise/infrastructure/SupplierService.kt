package org.deblock.exercise.infrastructure

import org.deblock.exercise.domain.model.Flight

interface SupplierService {
    fun getFlights(
        origin: String,
        destination: String,
        departureDate: String,
        returnDate: String,
        passengerCount: String
    ): List<Flight>
}
package org.deblock.exercise.domain.dto

import org.deblock.exercise.domain.model.Flight
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun CrazyAirSupplierDto.toFlight(supplier: String): Flight {
    return Flight(
        airline = this.airline,
        supplier = supplier,
        fare = this.price.toBigDecimal().setScale(2, RoundingMode.HALF_UP),
        departureAirportCode = this.departureAirportCode,
        destinationAirportCode = this.destinationAirportCode,
        departureDate = stringToLocalDateTime(this.departureDate, "yyyy-MM-dd'T'HH:mm:ss.SSS"),
        arrivalDate = stringToLocalDateTime(this.arrivalDate, "yyyy-MM-dd'T'HH:mm:ss.SSS"),
    )
}

fun stringToLocalDateTime(dateString: String, pattern: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDateTime.parse(dateString, formatter)
}
package org.deblock.exercise.domain.dto

data class CrazyAirSupplierDto(
    val airline: String,
    val price: Double,
    val cabinclass: String?,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: String,
    val arrivalDate: String
) {

}

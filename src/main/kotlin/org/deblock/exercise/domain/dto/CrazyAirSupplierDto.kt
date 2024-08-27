package org.deblock.exercise.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.deblock.exercise.domain.dto.deserializer.BigDecimalDeserializer
import java.math.BigDecimal

data class CrazyAirSupplierDto(
    val airline: String,
    @JsonDeserialize(using = BigDecimalDeserializer::class)
    val price: BigDecimal,
    @JsonProperty("cabinclass")
    val cabinClass: String,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: String,
    val arrivalDate: String
) {

}

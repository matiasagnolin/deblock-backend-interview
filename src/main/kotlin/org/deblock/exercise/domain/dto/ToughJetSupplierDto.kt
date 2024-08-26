package org.deblock.exercise.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.deblock.exercise.domain.dto.deserializer.IsoMomentDeserializer
import org.deblock.exercise.domain.dto.deserializer.PercentageDeserializer
import java.time.LocalDateTime


data class ToughJetSupplierDto(
    val carrier: String,
    val basePrice: Double,
    val tax: Double,
    @JsonDeserialize(using = PercentageDeserializer::class)
    @JsonProperty("discount")
    val discount: Double,
    val departureAirportName: String,
    val arrivalAirportName: String,
    @JsonDeserialize(using = IsoMomentDeserializer::class)
    val outboundDateTime: LocalDateTime,
    @JsonDeserialize(using = IsoMomentDeserializer::class)
    val inboundDateTime: LocalDateTime,
) {
}
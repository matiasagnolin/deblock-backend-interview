package org.deblock.exercise.domain.dto

import org.deblock.exercise.domain.model.Flight
import java.math.BigDecimal
import java.math.RoundingMode

fun ToughJetSupplierDto.toFlight(supplier: String): Flight {
    return Flight(
        airline = this.carrier,
        supplier = supplier,
        fare = calculateTotalPrice(),
        departureAirportCode = this.departureAirportName,
        destinationAirportCode = this.arrivalAirportName,
        departureDate = this.outboundDateTime,
        arrivalDate = this.inboundDateTime
    )
}

fun ToughJetSupplierDto.calculateTotalPrice(): BigDecimal {
    val priceWithTax = basePrice + tax
    val discountAmount = priceWithTax * BigDecimal(discount / 100)
    val totalPrice = priceWithTax - discountAmount
    return totalPrice.setScale(2, RoundingMode.HALF_UP)
}



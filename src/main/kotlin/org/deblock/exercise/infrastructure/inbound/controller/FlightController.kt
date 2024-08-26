package org.deblock.exercise.infrastructure.inbound.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.deblock.exercise.application.FlightSearcherService
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.infrastructure.inbound.validation.IATACode
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Tag(name = "Flight Controller", description = "Operations pertaining to flights")
@RestController
@RequestMapping("/api/v1")
@Validated
class FlightController(private val flightService: FlightSearcherService) {


    @Operation(
        method = "searchFlights",
        operationId = "searchFlights",
        description = "Search flights for params",
        responses = [
            ApiResponse(
                description = "flights found",
                responseCode = "200",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE
                )]
            ),
            ApiResponse(
                description = "bad request response",
                responseCode = "400"
            )]
    )

    @GetMapping("/flights")
    fun searchFlights(
        @Parameter(
            description = "Origin airport IATA code",
            required = true
        ) @RequestParam(required = true) @IATACode origin: String,
        @Parameter(
            description = "Destination airport IATA code",
            required = true
        ) @RequestParam(required = true) @IATACode destination: String,
        @Parameter(
            description = "Departure date",
            required = true
        ) @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) departureDate: LocalDate,
        @Parameter(
            description = "Return date",
            required = true
        ) @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) returnDate: LocalDate,
        @Parameter(
            description = "Number of passengers",
            required = true
        ) @RequestParam(required = true) @Valid @Min(1) @Max(4) numberOfPassengers: Int
    ): List<Flight> {
        return flightService.findFlights(origin, destination, departureDate, returnDate, numberOfPassengers)
    }

}
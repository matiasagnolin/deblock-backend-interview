package org.deblock.exercise.application

import kotlinx.coroutines.runBlocking
import org.deblock.exercise.BaseTest
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.infrastructure.SupplierService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate

class FlightSearcherServiceTest : BaseTest() {
    private lateinit var supplierService1: SupplierService
    private lateinit var supplierService2: SupplierService
    private lateinit var flightSearcherService: FlightSearcherServiceImpl

    @BeforeEach
    fun setUp() {
        supplierService1 = Mockito.mock(SupplierService::class.java)
        supplierService2 = Mockito.mock(SupplierService::class.java)

        // Initialize FlightSearcherServiceImpl with mocked SupplierService instances
        flightSearcherService = FlightSearcherServiceImpl(listOf(supplierService1, supplierService2))
    }

    @Test
    fun `should return sorted list of flights from multiple suppliers`() = runBlocking {
        // Given
        val departureDate = LocalDate.parse("2024-08-24")
        val returnDate = LocalDate.parse("2024-08-30")

        val flight1 = Flight(
            "Airline1",
            "Crazy-Air",
            BigDecimal(100.00),
            "BKK",
            "LND",
            departureDate.atStartOfDay(),
            returnDate.atStartOfDay()
        )
        val flight2 = Flight(
            "Airline2",
            "Tough-Jet",
            BigDecimal(110.00),
            "BKK",
            "EZE",
            departureDate.atStartOfDay(),
            returnDate.atStartOfDay()
        )
        val flight3 = Flight(
            "Airline3",
            "Tough-Jet",
            BigDecimal(10.00),
            "BKK",
            "MDA",
            departureDate.atStartOfDay(),
            returnDate.atStartOfDay()
        )

        //When
        `when`(supplierService1.getFlights(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(
            listOf(flight1, flight2)
        )
        `when`(supplierService2.getFlights(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(
            listOf(flight3)
        )

        val result = flightSearcherService.findFlights(
            "BKK",
            "BCN",
            LocalDate.parse("2024-08-24"),
            LocalDate.parse("2024-08-30"),
            2
        )

        //Then
        assertEquals(3, result.size)
        assertEquals(listOf(flight3, flight1, flight2), result)
    }


    @Test
    fun `when external services return empty result should not fail and return empty result`() = runBlocking {
        // Given
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        //When
        `when`(supplierService1.getFlights(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(
            emptyList()
        )
        `when`(supplierService2.getFlights(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(
            emptyList()
        )

        val result = flightSearcherService.findFlights(
            "BKK",
            "BCN",
            LocalDate.parse("2024-08-24"),
            LocalDate.parse("2024-08-30"),
            2
        )

        //Then
        assertEquals(0, result.size)
    }

}
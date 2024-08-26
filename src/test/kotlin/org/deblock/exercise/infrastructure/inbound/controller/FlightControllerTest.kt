package org.deblock.exercise.infrastructure.inbound.controller

import org.deblock.exercise.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat

@AutoConfigureMockMvc
class FlightControllerTest : BaseTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return bad request for invalid IATA code`() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val departureDate = dateFormat.format(dateFormat.parse("2024-08-24"))
        val returnDate = dateFormat.format(dateFormat.parse("2024-08-30"))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/flights")
                .param("origin", "FSKAKFA")  // Invalid IATA code
                .param("destination", "BCN")
                .param("departureDate", departureDate)
                .param("returnDate", returnDate)
                .param("numberOfPassengers", "2")
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.details").value("searchFlights.origin: must be a 3-letter IATA code"))  // Assuming this is your error message
    }

    @Test
    fun `should return bad request for number of passengers exceeding maximum`() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val departureDate = dateFormat.format(dateFormat.parse("2024-08-24"))
        val returnDate = dateFormat.format(dateFormat.parse("2024-08-30"))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/flights")
                .param("origin", "BKK")
                .param("destination", "BCN")
                .param("departureDate", departureDate)
                .param("returnDate", returnDate)
                .param("numberOfPassengers", "5")  // Exceeds maximum allowed passengers
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.details").value("searchFlights.numberOfPassengers: must be less than or equal to 4"))  // Assuming this is your error message
    }
}
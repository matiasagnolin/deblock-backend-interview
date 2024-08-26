package org.deblock.exercise

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.ResourceUtils
import org.springframework.web.context.WebApplicationContext
import java.nio.file.Files


@ExtendWith(WireMockExtension::class)
@AutoConfigureMockMvc
class FlightSearcherAcceptanceTest : BaseTest() {


    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    private lateinit var wireMockServer1: WireMockServer
    private lateinit var wireMockServer2: WireMockServer

    private val httpClient = OkHttpClient()

    @BeforeEach
    fun setup(): Unit {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        wireMockServer1 = WireMockServer(WireMockConfiguration.wireMockConfig().port(8088))
        wireMockServer2 = WireMockServer(WireMockConfiguration.wireMockConfig().port(8089))

        wireMockServer1.start()
        wireMockServer2.start()

        if (!wireMockServer1.isRunning && !wireMockServer2.isRunning) {
            throw IllegalStateException("WireMock servers are not running")
        }

    }


    @AfterEach
    fun teardown() {
        wireMockServer1.stop()
        wireMockServer2.stop()
    }

    @Test
    fun `should return test response from WireMock`() {
        //Given
        wireMockServer1.stubFor(
            WireMock.get(WireMock.urlEqualTo("/test-endpoint"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody("Test response")
                )
        )
        //When
        val request = Request.Builder()
            .url("http://localhost:8088/test-endpoint")
            .build()
        val response: Response = httpClient.newCall(request).execute()

        //Then
        assertEquals(200, response.code)
        assertEquals("Test response", response.body?.string())
    }


    @Test
    fun `should return all the available flights for all suppliers`() {
        //Given
        val expectedJson = Files.readString(ResourceUtils.getFile("classpath:response.json").toPath())

        val departureDate = "2024-08-24"
        val arrivalDate = "2024-08-30"


        wireMockServer1.stubFor(
            get(urlPathEqualTo("/flights"))
                .withQueryParam("origin", equalTo("BKK"))
                .withQueryParam("destination", equalTo("BCN"))
                .withQueryParam("departureDate", equalTo(departureDate))
                .withQueryParam("returnDate", equalTo(arrivalDate))
                .withQueryParam("numberOfPassengers", equalTo("2"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBodyFile("crazy-air.json")
                )
        )

        wireMockServer2.stubFor(
            get(urlPathEqualTo("/flights"))
                .withQueryParam("from", equalTo("BKK"))
                .withQueryParam("to", equalTo("BCN"))
                .withQueryParam("outboundDate", equalTo(departureDate))
                .withQueryParam("inboundDate", equalTo(arrivalDate))
                .withQueryParam("numberOfAdults", equalTo("2"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBodyFile("tough-jet.json")
                )
        )

        //When
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/flights")
                .param("origin", "BKK")
                .param("destination", "BCN")
                .param("departureDate", departureDate)
                .param("returnDate", arrivalDate)
                .param("numberOfPassengers", "2")
        )//then
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedJson))
    }


    @Test
    fun `when external services fail should return empty array`() {

        wireMockServer1.stop()
        wireMockServer2.stop()

        //Given
        val expectedJson = Files.readString(ResourceUtils.getFile("classpath:response.json").toPath())

        val departureDate = "2024-08-24"
        val arrivalDate = "2024-08-30"


        wireMockServer1.stubFor(
            get(urlPathEqualTo("/flights"))
                .withQueryParam("origin", equalTo("BKK"))
                .withQueryParam("destination", equalTo("BCN"))
                .withQueryParam("departureDate", equalTo(departureDate))
                .withQueryParam("returnDate", equalTo(arrivalDate))
                .withQueryParam("numberOfPassengers", equalTo("2"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBodyFile("crazy-air.json")
                )
        )

        wireMockServer2.stubFor(
            get(urlPathEqualTo("/flights"))
                .withQueryParam("from", equalTo("BKK"))
                .withQueryParam("to", equalTo("BCN"))
                .withQueryParam("outboundDate", equalTo(departureDate))
                .withQueryParam("inboundDate", equalTo(arrivalDate))
                .withQueryParam("numberOfAdults", equalTo("2"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBodyFile("tough-jet.json")
                )
        )

        //When
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/flights")
                .param("origin", "BKK")
                .param("destination", "BCN")
                .param("departureDate", departureDate)
                .param("returnDate", arrivalDate)
                .param("numberOfPassengers", "2")
        )//then
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("[]"))
    }



}

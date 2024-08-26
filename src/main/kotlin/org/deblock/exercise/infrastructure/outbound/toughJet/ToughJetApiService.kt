package org.deblock.exercise.infrastructure.outbound.toughJet

import org.deblock.exercise.domain.dto.ToughJetSupplierDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ToughJetApiService {

    @GET("/flights")
    fun getFlights(
        @Query("from") origin: String,
        @Query("to") destination: String,
        @Query("outboundDate") departureDate: String,
        @Query("inboundDate") returnDate: String,
        @Query("numberOfAdults") numberOfPassengers: String
    ): Call<List<ToughJetSupplierDto>>

}



package org.deblock.exercise.infrastructure.outbound.crazyAir

import org.deblock.exercise.domain.dto.CrazyAirSupplierDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CrazyAirApiService {

    @GET("/flights")
    fun getFlights(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("departureDate") departureDate: String,
        @Query("returnDate") returnDate: String,
        @Query("numberOfPassengers") numberOfPassengers: String
    ): Call<List<CrazyAirSupplierDto>>

}



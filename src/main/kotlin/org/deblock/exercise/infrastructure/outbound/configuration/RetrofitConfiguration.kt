package org.deblock.exercise.infrastructure.outbound.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import org.deblock.exercise.infrastructure.outbound.crazyAir.CrazyAirApiService
import org.deblock.exercise.infrastructure.outbound.toughJet.ToughJetApiService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.adapter.java8.Java8CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit


@Configuration
class RetrofitConfiguration(
    @Value("\${external.url.crazy-air}") val crazyAirUrl: String,
    @Value("\${external.url.tough-jet}") val toughJetUrl: String,
    @Value("\${microservices.connection-timeout}") val connectionTimeout: Long,
    @Value("\${microservices.timeout}") val timeout: Long,
    @Value("\${microservices.retry.max-attempts}") val maxRetries: Int
) {

    @Bean
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectionPool(ConnectionPool(20, 5, TimeUnit.MINUTES))
        .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
        .callTimeout(timeout, TimeUnit.MILLISECONDS)
        .addInterceptor(RetryInterceptor(maxRetries))
        .build()

    @Bean
    fun retrofitCrazyAirServer(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(crazyAirUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Bean
    fun retrofitToughJetServer(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(toughJetUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Bean
    fun crazyAirService(@Qualifier("retrofitCrazyAirServer") retrofit: Retrofit): CrazyAirApiService =
        retrofit.create(CrazyAirApiService::class.java)

    @Bean
    fun toughJetService(@Qualifier("retrofitToughJetServer") retrofit: Retrofit): ToughJetApiService =
        retrofit.create(ToughJetApiService::class.java)

}
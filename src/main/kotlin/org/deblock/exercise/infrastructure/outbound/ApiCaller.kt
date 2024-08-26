package org.deblock.exercise.infrastructure.outbound

import org.deblock.exercise.infrastructure.inbound.filter.RequestIdFilter
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import retrofit2.Call
import retrofit2.Response

@Service
class ApiCaller<T>(
    @Value("\${services.paths.microservice-delay}")
    private val delayThreshold: Long
) {

    private val log = LoggerFactory.getLogger(javaClass)


    fun execute(apiCall: () -> Call<T>): Response<T> {

        val requestId = MDC.get(RequestIdFilter.REQUEST_ID)
        val start = System.currentTimeMillis()

        var response = apiCall.invoke().execute()

        (System.currentTimeMillis() - start).let { delay ->
            if (delay > delayThreshold) {
                log.warn("[$requestId] Call took: ${delay}ms (delay threshold: ${delayThreshold}ms)")
            }
        }

        return response
    }
}
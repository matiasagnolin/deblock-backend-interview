package org.deblock.exercise.infrastructure.outbound.configuration

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetries: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        var attempt = 0
        var lastException: IOException? = null

        while (attempt < maxRetries) {
            try {
                response = chain.proceed(chain.request())
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                lastException = e
            }
            attempt++
        }

        throw lastException ?: IOException("External error during retry")
    }
}
package org.deblock.exercise.infrastructure.inbound.filter

import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

@Component
class RequestIdFilter : Filter {
    companion object {
        const val REQUEST_ID = "requestId"
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val requestId = UUID.randomUUID().toString()
        MDC.put(REQUEST_ID, requestId)
        try {
            chain.doFilter(request, response)
        } finally {
            MDC.remove(REQUEST_ID)
        }
    }

    override fun init(filterConfig: FilterConfig) {}

    override fun destroy() {}
}
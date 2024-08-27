package org.deblock.exercise.domain.dto.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import java.math.BigDecimal

class BigDecimalDeserializer : JsonDeserializer<BigDecimal>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): BigDecimal {
        val value = p.text
        return value.toBigDecimal()
    }
}
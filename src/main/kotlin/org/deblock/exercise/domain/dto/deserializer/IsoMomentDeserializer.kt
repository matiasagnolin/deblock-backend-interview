package org.deblock.exercise.domain.dto.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class IsoMomentDeserializer : JsonDeserializer<LocalDateTime>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime {
        val value = p.text
        val instant = Instant.parse(value)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        return zonedDateTime.toLocalDateTime()
    }
}

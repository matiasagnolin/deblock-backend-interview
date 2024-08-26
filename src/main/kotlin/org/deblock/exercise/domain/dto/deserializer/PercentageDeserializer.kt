package org.deblock.exercise.domain.dto.deserializer
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException

class PercentageDeserializer : JsonDeserializer<Double>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Double {
        val value = p.text
        // Remove '%' and parse the remaining part as a double
        return value.replace("%", "").trim().toDouble() / 100.0
    }
}

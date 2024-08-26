package org.deblock.exercise.infrastructure.inbound

data class ErrorResponse(
    val status: Int,
    val message: String,
    val details: List<String>?
)
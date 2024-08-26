package org.deblock.exercise.infrastructure.inbound

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException
    ): ResponseEntity<ErrorResponse> {
        val errorMessages = ex.constraintViolations
            .map { violation -> "${violation.propertyPath}: ${violation.message}" }

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Validation failed for one or more fields.",
            details = errorMessages
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {
        val errorMessages = ex.bindingResult.fieldErrors
            .map { fieldError -> "${fieldError.field}: ${fieldError.defaultMessage}" }

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Validation failed for one or more fields.",
            details = errorMessages
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
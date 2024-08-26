package org.deblock.exercise.infrastructure.inbound.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [IATACodeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class IATACode(
    val message: String = "must be a 3-letter IATA code",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

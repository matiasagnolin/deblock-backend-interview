package org.deblock.exercise.infrastructure.inbound.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class IATACodeValidator : ConstraintValidator<IATACode, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value != null && value.length == 3
    }
}
package com.ead.authuser.validation.validator

import com.ead.authuser.validation.UserNameConstraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component

@Component
class UserNameConstraintValidator: ConstraintValidator<UserNameConstraint, String> {

    override fun initialize(constraintAnnotation: UserNameConstraint?) {
        super.initialize(constraintAnnotation)
    }

    override fun isValid(userName: String?, context: ConstraintValidatorContext?): Boolean {
        return !(userName == null || userName.trim().isEmpty() || userName.contains(" "))
    }
}
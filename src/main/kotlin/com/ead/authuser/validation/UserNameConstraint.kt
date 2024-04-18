package com.ead.authuser.validation

import com.ead.authuser.validation.validator.UserNameConstraintValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [UserNameConstraintValidator::class])
@Target(*[AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY], AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class UserNameConstraint(

    val message: String = "Invalid username.",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []

) {}
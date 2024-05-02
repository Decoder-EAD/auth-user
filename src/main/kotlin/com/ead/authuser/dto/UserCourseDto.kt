package com.ead.authuser.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class UserCourseDto(

    @NotNull
    val courseId: UUID

) {}

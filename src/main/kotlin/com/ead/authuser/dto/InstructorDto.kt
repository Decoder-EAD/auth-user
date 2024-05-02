package com.ead.authuser.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class InstructorDto(

    @NotNull
    val userId: UUID

) {}

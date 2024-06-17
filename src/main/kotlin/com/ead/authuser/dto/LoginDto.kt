package com.ead.authuser.dto

import jakarta.validation.constraints.NotBlank

data class LoginDto(

    @NotBlank
    val username: String,

    @NotBlank
    val password: String

)
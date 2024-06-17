package com.ead.authuser.dto

data class JwtDto(

    val token: String,
    val type: String = "Bearer"

)

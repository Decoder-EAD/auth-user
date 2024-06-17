package com.ead.authuser.dto

import com.ead.authuser.models.UserModel
import java.util.UUID

data class UserEventDto(

    val userId: UUID,
    val userName: String,
    val email: String,
    val fullName: String,
    val userStatus: String,
    val userType: String,
    val cpf: String?,
    val imageUrl: String?,
    var actionType: String? = null

) {

    constructor(source: UserModel) : this(
        source.userId,
        source.userName,
        source.email,
        source.fullName,
        source.userStatus.toString(),
        source.userType.toString(),
        source.cpf,
        source.imageUrl
    )

}
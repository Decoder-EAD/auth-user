package com.ead.authuser.service

import com.ead.authuser.models.UserModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

interface UserService {

    fun getUsers(): List<UserModel>

    fun getUsers(pageable: Pageable, spec : Specification<UserModel>?): Page<UserModel>

    fun getUserById(userId: UUID): UserModel?

    fun existsByUserName(userName: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun create(userModel: UserModel): UserModel

    fun delete(userModel: UserModel)

    fun update(userModel: UserModel): UserModel

    fun updatePassword(userModel: UserModel): UserModel

}
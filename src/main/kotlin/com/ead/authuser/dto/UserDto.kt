package com.ead.authuser.dto

import com.ead.authuser.validation.UserNameConstraint
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto (

    var userId: UUID? = null,

    @field:UserNameConstraint(groups = [UserView.RegistrationPost::class])
    @field:NotBlank(groups = [UserView.RegistrationPost::class])
    @field:Size(min = 4, max = 50, groups = [UserView.RegistrationPost::class])
    @field:JsonView(UserView.RegistrationPost::class)
    var userName: String = "",

    @field:Email(groups = [UserView.RegistrationPost::class])
    @field:NotBlank(groups = [UserView.RegistrationPost::class])
    @JsonView(UserView.RegistrationPost::class)
    var email: String = "",

    @field:Size(min = 6, max = 30, groups = [UserView.RegistrationPost::class, UserView.PasswordPut::class])
    @field:NotBlank(groups = [UserView.RegistrationPost::class, UserView.PasswordPut::class])
    @field:JsonView(UserView.RegistrationPost::class, UserView.PasswordPut::class)
    var password: String = "",

    @field:Size(min = 6, max = 30, groups = [UserView.PasswordPut::class])
    @field:NotBlank(groups = [UserView.PasswordPut::class])
    @field:JsonView(UserView.PasswordPut::class)
    var oldPassword: String = "",

    @field:JsonView(UserView.RegistrationPost::class, UserView.UserPut::class)
    var fullName: String = "",

    @field:JsonView(UserView.RegistrationPost::class, UserView.UserPut::class)
    var phoneNumber: String? = null,

    @field:JsonView(UserView.RegistrationPost::class, UserView.UserPut::class)
    var cpf: String? = null,

    @field:NotBlank(groups = [UserView.ImagePut::class])
    @field:JsonView(UserView.ImagePut::class)
    var imageUrl: String? = null

) {

}

interface UserView {
    interface RegistrationPost {}
    interface UserPut {}
    interface PasswordPut {}
    interface ImagePut {}
}
package com.ead.authuser.models

import com.ead.authuser.enums.UserStatus
import com.ead.authuser.enums.UserType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import org.springframework.hateoas.RepresentationModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Entity
@Table(name = "TB_USERS")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserModel(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var userId: UUID? = null,

    @Column(nullable = false, unique = true, length = 50)
    var userName: String = "",

    @Column(nullable = false, unique = true, length = 50)
    var email: String = "",

    @JsonIgnore
    @Column(nullable = false, length = 255)
    var password: String = "",

    @Column(nullable = false, length = 150)
    var fullName: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var userStatus: UserStatus = UserStatus.ACTIVE,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var userType: UserType = UserType.STUDENT,

    @Column(length = 20)
    var phoneNumber: String? = null,

    @Column(length = 20)
    var cpf: String? = null,

    @Column
    var imageUrl: String? = null,

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss")
    var creationDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss")
    var updateDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),

    ) : RepresentationModel<UserModel>() {}

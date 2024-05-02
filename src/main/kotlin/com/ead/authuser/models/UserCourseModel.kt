package com.ead.authuser.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "TB_USERS_COURSES")
data class UserCourseModel(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var user: UserModel,

    @Column(nullable = false)
    var courseId: UUID

) {}
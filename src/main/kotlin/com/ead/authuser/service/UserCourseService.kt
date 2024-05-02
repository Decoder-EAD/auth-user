package com.ead.authuser.service

import com.ead.authuser.models.UserCourseModel
import com.ead.authuser.models.UserModel
import java.util.UUID

interface UserCourseService {

    fun existsByUserAndCourseId(userModel: UserModel, courseId: UUID): Boolean
    fun save(userCourseModel: UserCourseModel): UserCourseModel

}
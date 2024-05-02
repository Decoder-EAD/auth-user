package com.ead.authuser.repository

import com.ead.authuser.models.UserCourseModel
import com.ead.authuser.models.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserCourseRepository: JpaRepository<UserCourseModel, UUID> {

    fun existsByUserAndCourseId(userModel: UserModel, courseId:UUID): Boolean

}
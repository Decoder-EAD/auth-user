package com.ead.authuser.service.impl

import com.ead.authuser.models.UserCourseModel
import com.ead.authuser.models.UserModel
import com.ead.authuser.repository.UserCourseRepository
import com.ead.authuser.service.UserCourseService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserCourseServiceImpl(

    private val userCourseRepository: UserCourseRepository

) : UserCourseService {

    override fun existsByUserAndCourseId(userModel: UserModel, courseId: UUID): Boolean =
        userCourseRepository.existsByUserAndCourseId(userModel, courseId)

    override fun save(userCourseModel: UserCourseModel): UserCourseModel = userCourseRepository.save(userCourseModel)
}
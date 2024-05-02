package com.ead.authuser.dto

import com.ead.course.enums.CourseLevel
import com.ead.course.enums.CourseStatus
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CourseDto(

    var courseId: UUID? = null,
    var name: String = "",
    var description: String = "",
    var imageUrl: String? = null,
    var courseStatus: CourseStatus = CourseStatus.IN_PROGRESS,
    var courseLevel: CourseLevel = CourseLevel.BEGINNER,
    var userInstructor: UUID? = null

) {

}

package com.ead.authuser.controller

import com.ead.authuser.client.CourseClient
import com.ead.authuser.dto.CourseDto
import com.ead.authuser.dto.UserCourseDto
import com.ead.authuser.service.UserCourseService
import com.ead.authuser.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping(path = ["/users/{userId}/courses"], produces = [MediaType.APPLICATION_JSON_VALUE])
class UserCourseController(

    private val courseClient: CourseClient,
    private val userService: UserService,
    private val userCourseService: UserCourseService

) {

    @GetMapping
    fun getAllCoursesByUser(
        @PathVariable userId: UUID,
        @PageableDefault(page = 0, size = 10, sort = ["courseId"], direction = Sort.Direction.ASC) page: Pageable,
    ): ResponseEntity<Page<CourseDto>> = ResponseEntity.ok(courseClient.getAllCoursesByUser(userId, page))

    @PostMapping("/subscription")
    fun saveSubscriptionUserInCourse(
        @PathVariable userId: UUID,
        @RequestBody @Validated subscription: UserCourseDto
    ): ResponseEntity<Any> {

        val userModel = userService.getUserById(userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")

        if (userCourseService.existsByUserAndCourseId(userModel, subscription.courseId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Subscription already exists.")
        }

        userCourseService.save(userModel.convertToUserCourseModel(subscription.courseId))
        return ResponseEntity.ok("Subscription created successfully")

    }

}
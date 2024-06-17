package com.ead.authuser.controller

import com.ead.authuser.client.CourseClient
import com.ead.authuser.dto.CourseDto
import com.ead.authuser.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping(path = ["/users/{userId}/courses"], produces = [MediaType.APPLICATION_JSON_VALUE])
class UserCourseController(

    private val courseClient: CourseClient,
    private val userService: UserService

) {

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    fun getAllCoursesByUser(
        @PathVariable userId: UUID,
        @PageableDefault(page = 0, size = 10, sort = ["courseId"], direction = Sort.Direction.ASC) page: Pageable,
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String
    ): ResponseEntity<Page<CourseDto>> = ResponseEntity.ok(courseClient.getAllCoursesByUser(userId, page, token))

}
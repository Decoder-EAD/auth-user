package com.ead.authuser.controller

import com.ead.authuser.dto.UserDto
import com.ead.authuser.dto.UserView
import com.ead.authuser.models.UserModel
import com.ead.authuser.service.UserService
import com.ead.authuser.specifications.SpecificationTemplate
import com.ead.authuser.specifications.userCourseId
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@RestController
@RequestMapping(path = ["/users"], produces = [MediaType.APPLICATION_JSON_VALUE])
@CrossOrigin(origins = ["*"], maxAge = 3600)
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUserList(
        spec: SpecificationTemplate.UserSpec?,
        @PageableDefault(page = 0, size = 10, sort = ["userId"], direction = Sort.Direction.ASC) page: Pageable,
        @RequestParam(required = false) courseId: UUID?
    ): ResponseEntity<Page<UserModel>> {

        val userPage = if (courseId != null) {
            userService.getUsers(page, userCourseId(courseId).and(spec))
        } else {
            userService.getUsers(page, spec)
        }

        if (!userPage.isEmpty) {
            userPage.content.forEach {
                it.add(linkTo(methodOn(UserController::class.java).getUserById(it.userId!!)).withSelfRel())
            }
        }


        return ResponseEntity.ok(userPage)
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: UUID): ResponseEntity<Any> {
        val user = userService.getUserById(userId)

        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")
        }
    }

    @DeleteMapping("/{userId}")
    fun deleteUserById(@PathVariable userId: UUID): ResponseEntity<Any> {
        val user = userService.getUserById(userId)

        return if (user == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")
        } else {
            userService.deleteUserByID(userId)
            ResponseEntity.ok(user)
        }
    }

    @PutMapping("/{userId}")
    fun updateUserById(
        @PathVariable userId: UUID,
        @RequestBody @Validated(UserView.UserPut::class) @JsonView(UserView.UserPut::class) requestBody: UserDto
    ): ResponseEntity<Any> {
        val user = userService.getUserById(userId)

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")
        } else {
            user.fullName = requestBody.fullName
            user.phoneNumber = requestBody.phoneNumber
            user.cpf = requestBody.cpf
            user.updateDate = LocalDateTime.now(ZoneId.of("UTC"))

            userService.save(user)

            return ResponseEntity.ok(user)
        }
    }

    @PutMapping("/{userId}/password")
    fun updateUserPasswordById(
        @PathVariable userId: UUID,
        @RequestBody @Validated(UserView.PasswordPut::class) @JsonView(UserView.PasswordPut::class) requestBody: UserDto
    ): ResponseEntity<Any> {
        val user = userService.getUserById(userId)

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")
        } else {

            if (user.password != requestBody.oldPassword) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Mismatched old password!")
            }

            user.password = requestBody.password
            user.updateDate = LocalDateTime.now(ZoneId.of("UTC"))

            userService.save(user)

            return ResponseEntity.ok("Password successfully updated!")
        }
    }

    @PutMapping("/{userId}/image")
    fun updateUserImageById(
        @PathVariable userId: UUID,
        @RequestBody @Validated(UserView.ImagePut::class) @JsonView(UserView.ImagePut::class) requestBody: UserDto
    ): ResponseEntity<Any> {
        val user = userService.getUserById(userId)

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")
        } else {
            user.imageUrl = requestBody.imageUrl
            user.updateDate = LocalDateTime.now(ZoneId.of("UTC"))

            userService.save(user)

            return ResponseEntity.ok(user)
        }
    }
}
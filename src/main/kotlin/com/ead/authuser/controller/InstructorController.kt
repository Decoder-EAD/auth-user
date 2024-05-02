package com.ead.authuser.controller

import com.ead.authuser.config.Log
import com.ead.authuser.dto.InstructorDto
import com.ead.authuser.enums.UserType
import com.ead.authuser.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneId

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping(path = ["/instructors"], produces = [MediaType.APPLICATION_JSON_VALUE])
class InstructorController(

    private val userService: UserService

) {

    companion object: Log()

    @PostMapping("/subscription")
    fun saveInstructorSubscription(@RequestBody @Validated instructorDto: InstructorDto): ResponseEntity<Any> {

        val userModel = userService.getUserById(instructorDto.userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")

        userModel.userType = UserType.INSTRUCTOR
        userModel.updateDate = LocalDateTime.now(ZoneId.of("UTC"))
        userService.save(userModel)

        return ResponseEntity.ok(userModel)
    }

}
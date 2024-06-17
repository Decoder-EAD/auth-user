package com.ead.authuser.controller

import com.ead.authuser.config.Log
import com.ead.authuser.dto.InstructorDto
import com.ead.authuser.enums.RoleType
import com.ead.authuser.enums.UserType
import com.ead.authuser.service.RoleService
import com.ead.authuser.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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

    private val userService: UserService,
    private val roleService: RoleService

) {

    companion object: Log()

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/subscription")
    fun saveInstructorSubscription(@RequestBody @Validated instructorDto: InstructorDto): ResponseEntity<Any> {

        val userModel = userService.getUserById(instructorDto.userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")

        val roleModel = roleService.findByName(RoleType.ROLE_INSTRUCTOR)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Role not found.")

        userModel.userType = UserType.INSTRUCTOR
        userModel.updateDate = LocalDateTime.now(ZoneId.of("UTC"))
        userModel.roles.add(roleModel)
        userService.create(userModel)

        return ResponseEntity.ok(userModel)
    }

}
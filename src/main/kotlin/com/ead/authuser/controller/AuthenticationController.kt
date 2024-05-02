package com.ead.authuser.controller

import com.ead.authuser.config.Log
import com.ead.authuser.dto.UserDto
import com.ead.authuser.dto.UserView
import com.ead.authuser.models.UserModel
import com.ead.authuser.service.UserService
import com.fasterxml.jackson.annotation.JsonView
import org.apache.logging.log4j.LogManager
import org.springframework.beans.BeanUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/auth"], produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthenticationController(
    private val userService: UserService
) {

    companion object : Log()

    @PostMapping(path = ["/signup"])
    fun registerUser(
        @RequestBody @Validated(UserView.RegistrationPost::class) @JsonView(UserView.RegistrationPost::class) requestBody: UserDto
    ): ResponseEntity<Any> {
        log.info("Request received: $requestBody")
        if (userService.existsByUserName(requestBody.userName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken!")
        }
        if (userService.existsByEmail(requestBody.email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already taken!")
        }

        val userModel = UserModel()
        BeanUtils.copyProperties(requestBody, userModel)

        userService.save(userModel)

        return ResponseEntity.status(HttpStatus.CREATED).body(userModel)
    }

}
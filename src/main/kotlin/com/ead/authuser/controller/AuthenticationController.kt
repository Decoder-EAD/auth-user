package com.ead.authuser.controller

import com.ead.authuser.config.Log
import com.ead.authuser.dto.JwtDto
import com.ead.authuser.dto.LoginDto
import com.ead.authuser.dto.UserDto
import com.ead.authuser.dto.UserView
import com.ead.authuser.enums.RoleType
import com.ead.authuser.models.UserModel
import com.ead.authuser.security.JwtProvider
import com.ead.authuser.service.RoleService
import com.ead.authuser.service.UserService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.BeanUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/auth"], produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthenticationController(

    private val userService: UserService,
    private val roleService: RoleService,
    private val encoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtProvider: JwtProvider

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

        val roleModel = roleService.findByName(RoleType.ROLE_STUDENT)
            ?: return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Role not found.")

        requestBody.password = encoder.encode(requestBody.password)

        val userModel = UserModel()
        BeanUtils.copyProperties(requestBody, userModel)
        userModel.roles.add(roleModel)

        userService.create(userModel)

        return ResponseEntity.status(HttpStatus.CREATED).body(userModel)
    }

    @PostMapping(path = ["/login"])
    fun login(@RequestBody @Validated loginDto: LoginDto): ResponseEntity<JwtDto> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginDto.username, loginDto.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtProvider.generateJwt(authentication)
        return ResponseEntity.ok(JwtDto(jwt))
    }

}
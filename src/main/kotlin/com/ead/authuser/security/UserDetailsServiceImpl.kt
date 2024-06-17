package com.ead.authuser.security

import com.ead.authuser.repository.UserRepository
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserDetailsServiceImpl(

    private val userRepository: UserRepository

) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userModel = userRepository.findByUserName(username)
            ?: throw UsernameNotFoundException("Username $username not found")
        return UserDetailsImpl(userModel)
    }

    @Throws(AuthenticationCredentialsNotFoundException::class)
    fun loadUserById(userId: UUID): UserDetails {
        val userModel = userRepository.findByUserId(userId)
            ?: throw AuthenticationCredentialsNotFoundException("User ID $userId not found")

        return UserDetailsImpl(userModel)
    }

}
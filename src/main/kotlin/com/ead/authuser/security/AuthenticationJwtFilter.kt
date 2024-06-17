package com.ead.authuser.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class AuthenticationJwtFilter(

    private val jwtProvider: JwtProvider,
    private val userDetailsService: UserDetailsServiceImpl

): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwtStr = getTokenHeader(request)
            if (jwtStr != null && jwtProvider.validateJwt(jwtStr)) {
                val userId = jwtProvider.getSubjectJwt(jwtStr)
                val userDetails: UserDetails = userDetailsService.loadUserById(UUID.fromString(userId))
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Cannot set User Authentication: {}", e)
        }
        filterChain.doFilter(request, response)
    }

    private fun getTokenHeader(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length)
        }
        return null
    }
}
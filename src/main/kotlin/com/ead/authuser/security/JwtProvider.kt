package com.ead.authuser.security

import com.ead.authuser.config.Log
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import javax.crypto.SecretKey


@Component
class JwtProvider(

    @Value("\${ead.auth.jwtSecret}")
    private val jwtSecret: String,

    @Value("\${ead.auth.jwtExpiration}")
    private val jwtExpiration: Long

) {

    companion object : Log()

    fun generateJwt(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl

        return Jwts.builder()
            .subject(userPrincipal.userId.toString())
            .issuedAt(Date())
            .claim("roles", getRoles(userPrincipal))
            .expiration(Date(Date().time + jwtExpiration))
            .signWith(getSecretKey())
            .compact()
    }

    fun getSubjectJwt(token: String): String {
        return Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun validateJwt(authToken: String?): Boolean {
        try {
            Jwts.parser()
                .verifyWith(getSecretKey()).build()
                .parseSignedClaims(authToken)
            return true
        } catch (e: SecurityException) {
            log.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            log.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            log.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            log.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    private fun getSecretKey(): SecretKey? = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))

    private fun getRoles(userPrincipal: UserDetails): String? =
        userPrincipal.authorities.stream()
            .map { role -> role.authority }
            .collect(Collectors.joining(","))


}
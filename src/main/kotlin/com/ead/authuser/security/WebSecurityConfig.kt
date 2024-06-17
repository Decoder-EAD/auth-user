package com.ead.authuser.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(

    private val authenticationEntryPoint: AuthenticationEntryPointImpl,
    private val userDetailsService: UserDetailsService,
    private val authenticationJwtFilter: AuthenticationJwtFilter

) {

    companion object {
        val AUTH_WHITELIST = arrayOf(
            "/auth/**",
            "/actuator/**"
        )
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        val hierarchy = "ROLE_ADMIN > ROLE_INSTRUCTOR \n ROLE_INSTRUCTOR > ROLE_STUDENT \n ROLE_STUDENT > ROLE_USER"
        roleHierarchy.setHierarchy(hierarchy)
        return roleHierarchy
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.exceptionHandling { ex -> ex.authenticationEntryPoint(this.authenticationEntryPoint) }
            .sessionManagement { SessionCreationPolicy.STATELESS }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(*AUTH_WHITELIST).permitAll()
                auth.anyRequest().authenticated()
            }
            .formLogin(Customizer.withDefaults())
            .csrf { csrf -> csrf.disable() }
            .addFilterBefore(authenticationJwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(this.userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

}
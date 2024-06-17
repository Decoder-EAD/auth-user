package com.ead.authuser.security

import com.ead.authuser.models.UserModel
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID
import java.util.stream.Collectors

data class UserDetailsImpl(

    val userId: UUID,
    val fullName: String,
    private val username: String,
    private val email: String,
    @JsonIgnore private val password: String,
    private val authorities: MutableCollection<GrantedAuthority>

) : UserDetails {

    constructor(userModel: UserModel) : this(
        userId = userModel.userId,
        fullName = userModel.fullName,
        username = userModel.userName,
        email = userModel.email,
        password = userModel.password,
        authorities = userModel.roles.stream()
            .map { role -> SimpleGrantedAuthority(role.authority) }
            .collect(Collectors.toList())
    )

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = this.authorities

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
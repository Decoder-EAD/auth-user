package com.ead.authuser.models

import com.ead.authuser.enums.RoleType
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import java.util.UUID

@Entity
@Table(name = "TB_ROLES")
data class RoleModel(

    @Id
    val roleId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    val roleName: RoleType = RoleType.ROLE_STUDENT

): GrantedAuthority {

    override fun getAuthority(): String {
        return this.roleName.toString()
    }

}

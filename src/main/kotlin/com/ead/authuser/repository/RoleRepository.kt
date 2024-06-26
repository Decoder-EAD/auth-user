package com.ead.authuser.repository

import com.ead.authuser.enums.RoleType
import com.ead.authuser.models.RoleModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository: JpaRepository<RoleModel, UUID> {

    fun findByRoleName(roleName: RoleType): RoleModel?

}
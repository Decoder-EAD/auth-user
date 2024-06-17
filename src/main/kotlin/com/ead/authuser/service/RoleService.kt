package com.ead.authuser.service

import com.ead.authuser.enums.RoleType
import com.ead.authuser.models.RoleModel

interface RoleService {

    fun findByName(roleName: RoleType): RoleModel?

}
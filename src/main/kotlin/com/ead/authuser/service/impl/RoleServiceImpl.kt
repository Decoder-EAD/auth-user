package com.ead.authuser.service.impl

import com.ead.authuser.enums.RoleType
import com.ead.authuser.models.RoleModel
import com.ead.authuser.repository.RoleRepository
import com.ead.authuser.service.RoleService
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(

    val roleRepository: RoleRepository

): RoleService {

    override fun findByName(roleName: RoleType): RoleModel? = roleRepository.findByRoleName(roleName)
}
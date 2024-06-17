package com.ead.authuser.repository

import com.ead.authuser.models.UserModel
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {

    fun existsByUserName(userName: String): Boolean

    fun existsByEmail(email: String): Boolean

    @EntityGraph(attributePaths = ["roles"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByUserName(userName: String): UserModel?

    @EntityGraph(attributePaths = ["roles"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByUserId(userId: UUID): UserModel?

}
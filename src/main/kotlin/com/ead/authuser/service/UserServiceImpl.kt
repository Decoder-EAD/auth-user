package com.ead.authuser.service

import com.ead.authuser.models.UserModel
import com.ead.authuser.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun getUsers(): List<UserModel> = userRepository.findAll()

    override fun getUsers(pageable: Pageable, spec: Specification<UserModel>?): Page<UserModel> =
        if (spec != null) userRepository.findAll(spec, pageable) else userRepository.findAll(pageable)

    override fun getUserById(userId: UUID): UserModel? = userRepository.findByIdOrNull(userId)

    override fun deleteUserByID(userid: UUID) = userRepository.deleteById(userid)

    override fun save(userModel: UserModel): UserModel = userRepository.save(userModel)

    override fun existsByUserName(userName: String): Boolean = userRepository.existsByUserName(userName)

    override fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

}
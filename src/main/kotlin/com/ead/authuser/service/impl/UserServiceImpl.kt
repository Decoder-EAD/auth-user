package com.ead.authuser.service.impl

import com.ead.authuser.dto.UserEventDto
import com.ead.authuser.enums.ActionType
import com.ead.authuser.models.UserModel
import com.ead.authuser.publisher.UserEventPublisher
import com.ead.authuser.repository.UserRepository
import com.ead.authuser.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userEventPublisher: UserEventPublisher
) : UserService {

    override fun getUsers(): List<UserModel> = userRepository.findAll()

    override fun getUsers(pageable: Pageable, spec: Specification<UserModel>?): Page<UserModel> =
        if (spec != null) userRepository.findAll(spec, pageable) else userRepository.findAll(pageable)

    override fun getUserById(userId: UUID): UserModel? = userRepository.findByIdOrNull(userId)

    override fun existsByUserName(userName: String): Boolean = userRepository.existsByUserName(userName)

    override fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    @Transactional
    override fun create(userModel: UserModel): UserModel {
        userRepository.save(userModel)
        userEventPublisher.publishUserEvent(UserEventDto(userModel), ActionType.CREATE)
        return userModel
    }

    @Transactional
    override fun update(userModel: UserModel): UserModel {
        userRepository.save(userModel)
        userEventPublisher.publishUserEvent(UserEventDto(userModel), ActionType.UPDATE)
        return userModel
    }

    @Transactional
    override fun delete(userModel: UserModel) {
        userRepository.delete(userModel)
        userEventPublisher.publishUserEvent(UserEventDto(userModel), ActionType.DELETE)
    }

    override fun updatePassword(userModel: UserModel): UserModel {
        return create(userModel)
    }
}

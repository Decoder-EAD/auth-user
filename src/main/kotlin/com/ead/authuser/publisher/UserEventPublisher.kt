package com.ead.authuser.publisher

import com.ead.authuser.dto.UserEventDto
import com.ead.authuser.enums.ActionType
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class UserEventPublisher(

    @Value("\${ead.broker.exchange.user-event}")
    private val exchangeUserEvent: String,

    private val rabbitTemplate: RabbitTemplate

) {

    fun publishUserEvent(userEvent: UserEventDto, actionType: ActionType) {
        userEvent.actionType = actionType.toString()
        rabbitTemplate.convertAndSend(this.exchangeUserEvent, "", userEvent)
    }

}
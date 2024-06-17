package com.ead.authuser.specifications

import com.ead.authuser.models.UserModel
import jakarta.persistence.criteria.Join
import net.kaczmarzyk.spring.data.jpa.domain.Equal
import net.kaczmarzyk.spring.data.jpa.domain.Like
import net.kaczmarzyk.spring.data.jpa.web.annotation.And
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec
import org.springframework.data.jpa.domain.Specification
import java.util.*

class SpecificationTemplate {

    @And(
        Spec(path = "userType", spec = Equal::class),
        Spec(path = "userStatus", spec = Equal::class),
        Spec(path = "email", spec = Like::class),
        Spec(path = "fullName", spec = Like::class)
    )
    interface UserSpec : Specification<UserModel> {}

}

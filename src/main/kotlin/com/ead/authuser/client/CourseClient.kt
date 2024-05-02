package com.ead.authuser.client

import com.ead.authuser.config.Log
import com.ead.authuser.dto.CourseDto
import com.ead.authuser.dto.ResponsePageDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.*

@Component
class CourseClient(

    private val restTemplate: RestTemplate,

    @Value("\${ead.api.url.course}")
    private val requestUrl: String

) {

    companion object : Log()

    fun getAllCoursesByUser(userId: UUID, pageable: Pageable): Page<CourseDto> {
        try {
            val sortParam = pageable.sort.toString().replace(": ", ",")
            val url = "$requestUrl/courses?userId=$userId&page=${pageable.pageNumber}&size=${pageable.pageSize}&sort=$sortParam"
            log.info("Request URL: {}", url)

            val responseType = object : ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {}
            return restTemplate.exchange(url, HttpMethod.GET, null, responseType).body as Page<CourseDto>
        } catch (ex: HttpStatusCodeException) {
            log.error("Error request '/courses' {}", ex.message, ex)
        }
        return Page.empty()
    }



}
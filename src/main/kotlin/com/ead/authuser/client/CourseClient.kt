package com.ead.authuser.client

import com.ead.authuser.config.Log
import com.ead.authuser.dto.CourseDto
import com.ead.authuser.dto.ResponsePageDto
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.*

@Component
class CourseClient(

    private val restTemplate: RestTemplate,

    @Value("\${ead.api.course.url}")
    private val requestUrl: String

) {

    companion object : Log()

    @CircuitBreaker(name = "circuitbreaker-instance", fallbackMethod = "circuitbreakerFallback")
    fun getAllCoursesByUser(userId: UUID, pageable: Pageable, token: String): Page<CourseDto> {
        try {
            val sortParam = pageable.sort.toString().replace(": ", ",")
            val url = "$requestUrl/courses?userId=$userId&page=${pageable.pageNumber}&size=${pageable.pageSize}&sort=$sortParam"
            val headers = mapOf(HttpHeaders.AUTHORIZATION to token)
            val requestEntity = HttpEntity("parameters" to headers)
            log.info("Request URL: {}", url)

            val responseType = object : ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {}
            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType).body as Page<CourseDto>
        } catch (ex: HttpStatusCodeException) {
            log.error("Error request '/courses' {}", ex.message, ex)
        }
        return Page.empty()
    }

    private fun circuitbreakerFallback(userId: UUID, pageable: Pageable, throwable: Throwable): Page<CourseDto> {
        log.error("CircuitBreaker Fallback cause {}", throwable.toString())
        return PageImpl(emptyList())
    }

}
package com.ead.authuser.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestTemplateConfig {

    companion object {
        const val TIMEOUT = 5000L
    }

    @Bean
    @LoadBalanced
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder
            .setConnectTimeout(Duration.ofMillis(TIMEOUT))
            .setReadTimeout(Duration.ofMillis(TIMEOUT))
            .build()
    }

}
package com.ead.authuser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthUserApplication

fun main(args: Array<String>) {
	runApplication<AuthUserApplication>(*args)
}

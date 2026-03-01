package com.pgsc.tracko

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class TrackoApplication

fun main(args: Array<String>) {
    runApplication<TrackoApplication>(*args)
}

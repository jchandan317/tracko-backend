package com.pgsc.tracko

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrackoApplication

fun main(args: Array<String>) {
    runApplication<TrackoApplication>(*args)
}

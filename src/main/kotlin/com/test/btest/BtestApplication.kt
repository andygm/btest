package com.test.btest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BtestApplication

fun main(args: Array<String>) {
    runApplication<BtestApplication>(*args)
}

package com.hyecheon.springbatchbasic

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication
class SpringBatchBasicApplication

fun main(args: Array<String>) {
	runApplication<SpringBatchBasicApplication>(*args)
}

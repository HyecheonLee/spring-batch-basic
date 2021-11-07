package com.hyecheon.springbatchbasic.job

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/06
 */
@Configuration
class JobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory
) {


	@Bean("helloJob")
	fun helloJob(@Qualifier("helloStep") taskletStep: TaskletStep) = run {
		jobBuilderFactory.get("helloJob")
			.incrementer(RunIdIncrementer())
			.start(taskletStep)
			.build()
	}

	@JobScope
	@Bean
	fun helloStep(tasklet: Tasklet) = run {
		stepBuilderFactory.get("helloStep")
			.tasklet(tasklet)
			.build()
	}

	@StepScope
	@Bean
	fun tasklet() = run {
		Tasklet { contribution, chunkContext ->
			println("Hello Spring Batch")
			RepeatStatus.FINISHED
		}
	}
}
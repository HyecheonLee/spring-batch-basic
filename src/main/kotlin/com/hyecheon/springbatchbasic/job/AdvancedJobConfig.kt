package com.hyecheon.springbatchbasic.job

import com.hyecheon.springbatchbasic.job.validate.LocalDateParameterValidator
import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/20
 */
@Configuration
class AdvancedJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun advancedJob() = run {
		jobBuilderFactory.get("advancedJob")
			.incrementer(RunIdIncrementer())
			.validator(LocalDateParameterValidator("targetDate"))
			.listener(jobExecutionListener())
			.start(advancedStep())
			.build()
	}

	@JobScope
	@Bean
	fun jobExecutionListener() = run {
		object : JobExecutionListener {
			override fun beforeJob(jobExecution: JobExecution) {
				log.info("[JobExecutionListener#before] jobExecution is ${jobExecution.status}")
			}

			override fun afterJob(jobExecution: JobExecution) {
				if (jobExecution.status == BatchStatus.FAILED) {
					log.error("[JobExecutionListener#after] jobExecution is ${jobExecution.status}!!! Recovery")
				} else {
					log.info("[JobExecutionListener#after] jobExecution is ${jobExecution.status}")
				}
			}
		}
	}

	@JobScope
	@Bean
	fun advancedStep() = run {
		stepBuilderFactory.get("advancedStep")
			.tasklet(advancedTasklet())
			.build()
	}

	@StepScope
	@Bean
	fun advancedTasklet(@Value("#{jobParameters['targetDate']}") targetDate: String? = null) = run {
		Tasklet { contribution, chunkContext ->
			log.info("[advancedJobConfig]] JobParameter - targetDate = $targetDate")
			log.info("[advancedJobConfig]] executed advancedTasklet")
			RepeatStatus.FINISHED
		}
	}
}
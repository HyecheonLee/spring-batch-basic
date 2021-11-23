package com.hyecheon.springbatchbasic.job.parallel

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.partition.PartitionHandler
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.partition.support.SimplePartitioner
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/24
 */
@Configuration
class PartitioningJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory
) {
	private val log = LoggerFactory.getLogger(this::class.java)
	private val PartitionSize = 100

	@Bean
	fun partitioningJob(masterStep: Step) = run {
		jobBuilderFactory.get("partitioningJob")
			.incrementer(RunIdIncrementer())
			.start(masterStep)
			.build()
	}

	@JobScope
	@Bean
	fun masterStep(partitioner: Partitioner, partitionHandler: PartitionHandler) = run {
		stepBuilderFactory.get("masterStep")
			.partitioner("anotherStep", partitioner)
			.partitionHandler(partitionHandler)
			.build()
	}

	@StepScope
	@Bean
	fun partitioner() = run {
		val partitioner = SimplePartitioner()
		partitioner.partition(PartitionSize)
		partitioner
	}

	@StepScope
	@Bean
	fun partitionHandler(anotherStep: Step, taskExecutor: SimpleAsyncTaskExecutor) = run {
		TaskExecutorPartitionHandler().apply {
			step = anotherStep
			gridSize = PartitionSize
			setTaskExecutor(taskExecutor)
		}
	}
}
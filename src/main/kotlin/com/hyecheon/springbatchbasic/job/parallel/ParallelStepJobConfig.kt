package com.hyecheon.springbatchbasic.job.parallel

import com.hyecheon.springbatchbasic.dto.AmountDto
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.core.job.flow.support.SimpleFlow
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/24
 */
@Configuration
class ParallelStepJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun parallelJob(splitFlow: Flow) = run {
		jobBuilderFactory.get("parallelJob")
			.incrementer(RunIdIncrementer())
			.start(splitFlow)
			.build()
			.build()
	}

	@Bean
	fun splitFlow(
		taskExecutor: SimpleAsyncTaskExecutor, flowAmountFileStep: Flow, flowAnotherStep: Flow
	) = run {
		FlowBuilder<SimpleFlow>("parallelFlow")
			.split(taskExecutor)
			.add(flowAmountFileStep, flowAnotherStep)
			.build()
	}

	@Bean
	fun flowAmountFileStep(amountFileStep: Step) = run {
		FlowBuilder<SimpleFlow>("flowAmountFileStep")
			.start(amountFileStep)
			.end()
	}

	@Bean
	fun amountFileStep(
		amountFileItemReader: ItemReader<AmountDto>,
		amountFileItemProcessor: ItemProcessor<AmountDto, AmountDto>,
		amountFileItemWriter: FlatFileItemWriter<AmountDto>,
	) = run {
		stepBuilderFactory.get("multiThreadStep").chunk<AmountDto, AmountDto>(10)
			.reader(amountFileItemReader)
			.processor(amountFileItemProcessor)
			.writer(amountFileItemWriter)
			.build()
	}

	@Bean
	fun flowAnotherStep(anotherStep: Step) = run {
		FlowBuilder<SimpleFlow>("anotherStep")
			.start(anotherStep)
			.build()
	}

	@Bean
	fun anotherStep() = run {
		stepBuilderFactory.get("anotherStep")
			.tasklet { contribution, chunkContext ->
				Thread.sleep(500)
				log.info(Thread.currentThread().name)
				RepeatStatus.FINISHED
			}
			.build()
	}
}
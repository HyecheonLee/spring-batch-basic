package com.hyecheon.springbatchbasic.job

import com.hyecheon.springbatchbasic.core.domain.PlainText
import com.hyecheon.springbatchbasic.core.repository.PlainTextRepository
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/06
 */
@Configuration
class PlainTextJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory,
	private val plainTextRepository: PlainTextRepository
) {


	@Bean("plainTextJob")
	fun plainTextJob(@Qualifier("plainTextStep") taskletStep: TaskletStep) = run {
		jobBuilderFactory.get("plainTextJob")
			.incrementer(RunIdIncrementer())
			.start(taskletStep)
			.build()
	}

	@JobScope
	@Bean("plainTextStep")
	fun plainTextStep(
		plainTextReader: RepositoryItemReader<PlainText>,
		plainTextProcessor: ItemProcessor<PlainText, String>,
		plainTextWriter: ItemWriter<String>,
	) = run {
		stepBuilderFactory.get("plainTextStep")
			.chunk<PlainText, String>(5)
			.reader(plainTextReader)
			.processor(plainTextProcessor)
			.writer(plainTextWriter)
			.build()
	}

	@StepScope
	@Bean
	fun plainTextReader() = run {
		RepositoryItemReaderBuilder<PlainText>()
			.name("plainTextReader")
			.repository(plainTextRepository)
			.methodName("findBy")
			.pageSize(5)
			.arguments(mutableListOf<Any>())
			.sorts(Collections.singletonMap("id", Sort.Direction.DESC))
			.build()
	}

	@StepScope
	@Bean
	fun plainTextProcessor() = run {
		ItemProcessor<PlainText, String>() { item ->
			"processed ${item.text}"
		}
	}

	@StepScope
	@Bean
	fun plainTextWriter() = run {
		ItemWriter<String>() { items ->
			items.forEach { s -> println(s) }
			println("------- chunk is finished")
		}
	}
}
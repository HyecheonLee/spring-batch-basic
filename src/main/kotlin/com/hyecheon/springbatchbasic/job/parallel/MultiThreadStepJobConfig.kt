package com.hyecheon.springbatchbasic.job.parallel

import com.hyecheon.springbatchbasic.dto.AmountDto
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.task.SimpleAsyncTaskExecutor
import java.io.File

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/23
 */
@Configuration
class MultiThreadStepJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun multiThreadStepJob(multiThreadStep: Step? = null) = run {
		jobBuilderFactory.get("multiThreadStepJob")
			.incrementer(RunIdIncrementer())
			.start(multiThreadStep!!)
			.build()
	}

	@JobScope
	@Bean
	fun multiThreadStep(
		amountFileItemReader: ItemReader<AmountDto>? = null,
		amountFileItemProcessor: ItemProcessor<AmountDto, AmountDto>? = null,
		amountFileItemWriter: FlatFileItemWriter<AmountDto>? = null,
		taskExecutor: SimpleAsyncTaskExecutor? = null
	) = run {
		stepBuilderFactory.get("multiThreadStep")
			.chunk<AmountDto, AmountDto>(10)
			.reader(amountFileItemReader!!)
			.processor(amountFileItemProcessor!!)
			.writer(amountFileItemWriter!!)
			.taskExecutor(taskExecutor!!)
			.build()
	}

	@Bean
	fun taskExecutor() = run {
		SimpleAsyncTaskExecutor("spring-batch-task-executor")
	}

	@StepScope
	@Bean
	fun amountFileItemReader() = run {
		FlatFileItemReaderBuilder<AmountDto>()
			.name("amountFileItemReader")
			.fieldSetMapper(AmountFieldSetMapper())
			.lineTokenizer(DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB))
			.resource(FileSystemResource("data/input.txt"))
			.build()
	}

	@StepScope
	@Bean
	fun amountFileItemProcessor() = run {
		ItemProcessor<AmountDto, AmountDto> { item ->
			log.info(item.toString())
			item.copy(amount = item.amount * 100)
		}
	}

	@StepScope
	@Bean
	fun amountFileItemWriter() = run {
		val lineAggregator = BeanWrapperFieldExtractor<AmountDto>().apply {
			setNames(arrayOf("index", "name", "amount"))
			afterPropertiesSet()
		}.let {

			DelimitedLineAggregator<AmountDto>().apply {
				setFieldExtractor(it)
			}
		}

		val filePath = "data/output.txt"
		File(filePath).createNewFile()

		FlatFileItemWriterBuilder<AmountDto>()
			.name("amountFileItemWriter")
			.resource(FileSystemResource(filePath))
			.lineAggregator(lineAggregator)
			.build()
	}
}
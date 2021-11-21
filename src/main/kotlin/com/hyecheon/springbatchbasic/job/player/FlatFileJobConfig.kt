package com.hyecheon.springbatchbasic.job.player

import com.hyecheon.springbatchbasic.core.service.PlayerSalaryService
import com.hyecheon.springbatchbasic.dto.PlayerDto
import com.hyecheon.springbatchbasic.dto.PlayerSalaryDto
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.adapter.ItemProcessorAdapter
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import java.io.File

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/20
 */
@Configuration
class FlatFileJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory,
	private val playerSalaryService: PlayerSalaryService
) {
	@Bean
	fun flatFileJob() = run {
		jobBuilderFactory.get("flatFileJob")
			.incrementer(RunIdIncrementer())
			.start(flatFileStep())
			.build()
	}

	@JobScope
	@Bean
	fun flatFileStep(
		playerSalaryItemProcessorAdapter: ItemProcessorAdapter<PlayerDto, PlayerSalaryDto>? = null,
		playerFileItemWriter: FlatFileItemWriter<PlayerSalaryDto>? = null
	) = run {
		stepBuilderFactory.get("flatFileStep")
			.chunk<PlayerDto, PlayerSalaryDto>(5)
			.reader(playerFileItemReader())
			.processor(playerSalaryItemProcessorAdapter!!)
			.writer(playerFileItemWriter!!)
			.build()
	}

	@StepScope
	@Bean
	fun playerFileItemWriter() = run {
		val lineAggregator = BeanWrapperFieldExtractor<PlayerSalaryDto>().apply {
			setNames(arrayOf("id", "firstName", "lastName", "salary"))
			afterPropertiesSet()
		}.let { extractor ->
			DelimitedLineAggregator<PlayerSalaryDto>().apply {
				setDelimiter("\t")
				setFieldExtractor(extractor)
			}
		}

		val fileName = "player-salary-list.txt"
		File(fileName).createNewFile()
		val resource = FileSystemResource(fileName)


		FlatFileItemWriterBuilder<PlayerSalaryDto>()
			.name("playerFileItemWriter")
			.resource(resource)
			.lineAggregator(lineAggregator)
			.build()
	}

	@StepScope
	@Bean
	fun playerSalaryItemProcessorAdapter() = run {
		ItemProcessorAdapter<PlayerDto, PlayerSalaryDto>().apply {
			setTargetObject(playerSalaryService)
			setTargetMethod("calcSalary")
		}
	}

	@StepScope
	@Bean
	fun playerSalaryItemProcessor() = run {
		ItemProcessor<PlayerDto, PlayerSalaryDto> { item -> playerSalaryService.calcSalary(item) }
	}

	@StepScope
	@Bean
	fun playerFileItemReader() = run {
		FlatFileItemReaderBuilder<PlayerDto>()
			.name("playerFileItemReader")
			.lineTokenizer(DelimitedLineTokenizer())
			.linesToSkip(1)
			.fieldSetMapper(PlayerFieldSetMapper())
			.resource(FileSystemResource("player-list.txt"))
			.build()
	}
}
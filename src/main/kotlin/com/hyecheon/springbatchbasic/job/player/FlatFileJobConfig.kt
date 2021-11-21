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
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource

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
	fun flatFileStep(playerSalaryItemProcessorAdapter: ItemProcessorAdapter<PlayerDto, PlayerSalaryDto>? = null) = run {
		stepBuilderFactory.get("flatFileStep")
			.chunk<PlayerDto, PlayerSalaryDto>(5)
			.reader(playerFileItemReader())
			.processor(playerSalaryItemProcessorAdapter!!)
			.writer { items ->
				items.forEach { println(it) }
			}
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
package com.hyecheon.springbatchbasic.job

import com.hyecheon.springbatchbasic.BatchTestConfig
import com.hyecheon.springbatchbasic.core.domain.PlainText
import com.hyecheon.springbatchbasic.core.repository.PlainTextRepository
import com.hyecheon.springbatchbasic.core.repository.ResultTextRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/07
 */
@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@ContextConfiguration(classes = [PlainTextJobConfig::class, BatchTestConfig::class])
internal class PlainTextJobConfigTest {
	@Autowired
	lateinit var jobLauncherTestUtils: JobLauncherTestUtils

	@Autowired
	lateinit var plainTextRepository: PlainTextRepository

	@Autowired
	lateinit var resultTextRepository: ResultTextRepository

	@AfterEach
	internal fun tearDown() {
		plainTextRepository.deleteAll()
		resultTextRepository.deleteAll()
	}


	@DisplayName("1. 주어진 텍스트가 없을때")
	@Test
	internal fun test_1() {
		val execution = jobLauncherTestUtils.launchJob()
		Assertions.assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
		Assertions.assertThat(resultTextRepository.count()).isEqualTo(0)
	}


	@DisplayName("2. 주어진 텍스트가 있을때")
	@Test
	internal fun test_2() {
		//given
		givenPlainTexts(12)

		//when
		val execution = jobLauncherTestUtils.launchJob()

		//then
		Assertions.assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
		Assertions.assertThat(resultTextRepository.count()).isEqualTo(12)

	}

	fun givenPlainTexts(count: Int) = run {
		(1..count).forEach {
			val plainText = PlainText("text${it}")
			plainTextRepository.save(plainText)
		}
	}
}
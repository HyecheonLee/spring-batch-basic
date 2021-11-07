package com.hyecheon.springbatchbasic.job

import com.hyecheon.springbatchbasic.BatchTestConfig
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
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
@ContextConfiguration(classes = [HelloJobConfig::class, BatchTestConfig::class])
class HelloJobConfigTest {
	@Autowired
	lateinit var jobLauncherTestUtils: JobLauncherTestUtils


	@DisplayName("1. 성공")
	@Test
	fun test_1() {
		val execution = jobLauncherTestUtils.launchJob()
		Assertions.assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
	}
}
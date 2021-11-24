package com.hyecheon.springbatchbasic.job.player

import com.hyecheon.springbatchbasic.BatchTestConfig
import com.hyecheon.springbatchbasic.core.service.PlayerSalaryService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.test.AssertFile
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.FileSystemResource
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/24
 */
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [FlatFileJobConfig::class, BatchTestConfig::class, PlayerSalaryService::class])
internal class FlatFileJobConfigTest {

	@Autowired
	lateinit var jobLauncherTestUtils: JobLauncherTestUtils


	@DisplayName("1. 성공 테스트")
	@Test
	internal fun test_1() {
		//given

		//when
		val execution = jobLauncherTestUtils.launchJob()

		//then
		Assertions.assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
		AssertFile.assertFileEquals(
			FileSystemResource("player-salary-list.txt"),
			FileSystemResource("succeed-player-salary-list.txt")
		)
	}

}
package com.hyecheon.springbatchbasic.core.service

import com.hyecheon.springbatchbasic.dto.PlayerDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.Year

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/24
 */
internal class PlayerSalaryServiceTest {

	lateinit var playerSalaryService: PlayerSalaryService

	@BeforeEach
	internal fun setUp() {
		playerSalaryService = PlayerSalaryService()
	}

	@DisplayName("1. 셀러리 계산")
	@Test
	internal fun test_1() {
		//given
		val playerDto = PlayerDto("10", "", "", "", 1980, 10)

		val mockYear = mock(Year::class.java)
		given(mockYear.value).willReturn(2021)
		Mockito.mockStatic(Year::class.java).`when`<Year>(Year::now).thenReturn(mockYear)

		//when
		val result = playerSalaryService.calcSalary(playerDto)

		//then
		Assertions.assertThat(result.salary).isEqualTo(41000000)
	}

}
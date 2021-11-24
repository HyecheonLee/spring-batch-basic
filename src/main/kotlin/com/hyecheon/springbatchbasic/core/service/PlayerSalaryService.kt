package com.hyecheon.springbatchbasic.core.service

import com.hyecheon.springbatchbasic.dto.PlayerDto
import com.hyecheon.springbatchbasic.dto.PlayerSalaryDto
import org.springframework.stereotype.Service
import java.time.Year

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/21
 */
@Service
class PlayerSalaryService {

	fun calcSalary(player: PlayerDto) = run {
		val salary = (Year.now().value - player.birthYear) * 1000000
		PlayerSalaryDto.of(player, salary)
	}
}
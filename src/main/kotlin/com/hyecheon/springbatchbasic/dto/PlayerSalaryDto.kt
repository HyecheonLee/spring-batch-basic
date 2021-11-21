package com.hyecheon.springbatchbasic.dto

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/20
 */
data class PlayerSalaryDto(
	var id: String,
	var lastName: String,
	var firstName: String,
	var position: String,
	var birthYear: Int,
	var debutYear: Int,
	var salary: Int
) {
	companion object {
		fun of(player: PlayerDto, salary: Int) = run {
			PlayerSalaryDto(
				player.id,
				player.lastName,
				player.firstName,
				player.position,
				player.birthYear,
				player.debutYear,
				salary
			)
		}
	}
}
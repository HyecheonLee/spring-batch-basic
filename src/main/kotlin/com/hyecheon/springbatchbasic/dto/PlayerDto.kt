package com.hyecheon.springbatchbasic.dto

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/20
 */
data class PlayerDto(
	var id: String,
	var lastName: String,
	var firstName: String,
	var position: String,
	var birthYear: Int,
	var debutYear: Int
)
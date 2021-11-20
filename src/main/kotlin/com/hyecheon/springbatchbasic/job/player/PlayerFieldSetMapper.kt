package com.hyecheon.springbatchbasic.job.player

import com.hyecheon.springbatchbasic.dto.PlayerDto
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/20
 */
class PlayerFieldSetMapper : FieldSetMapper<PlayerDto> {
	override fun mapFieldSet(fieldSet: FieldSet): PlayerDto {
		return PlayerDto(
			fieldSet.readRawString(0),
			fieldSet.readRawString(1),
			fieldSet.readRawString(2),
			fieldSet.readRawString(3),
			fieldSet.readInt(4),
			fieldSet.readInt(5),
		)
	}
}
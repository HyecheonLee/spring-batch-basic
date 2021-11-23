package com.hyecheon.springbatchbasic.job.parallel

import com.hyecheon.springbatchbasic.dto.AmountDto
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/23
 */
class AmountFieldSetMapper : FieldSetMapper<AmountDto> {
	override fun mapFieldSet(fieldSet: FieldSet): AmountDto {
		return AmountDto(fieldSet.readInt(0), fieldSet.readString(1), fieldSet.readInt(2))
	}
}
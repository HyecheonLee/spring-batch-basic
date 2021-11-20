package com.hyecheon.springbatchbasic.job.validate

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/20
 */
class LocalDateParameterValidator(
	private val parameterName: String
) : JobParametersValidator {
	override fun validate(parameters: JobParameters?) {
		val localDate = parameters?.getString(parameterName)
		if (localDate.isNullOrBlank()) {
			throw JobParametersInvalidException("$parameterName 가 빈 문자열이거나 존재하지 않습니다.")

		}

		try {
			LocalDate.parse(localDate)
		} catch (e: DateTimeParseException) {
			throw JobParametersInvalidException("$parameterName 가 날짜 형식의 문자열이 아닙니다.")
		}
	}
}
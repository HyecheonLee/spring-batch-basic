package com.hyecheon.springbatchbasic.core.repository

import com.hyecheon.springbatchbasic.core.domain.PlainText
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/07
 */
interface PlainTextRepository : JpaRepository<PlainText, Long> {
	fun findBy(pageable: Pageable): Page<PlainText>
}
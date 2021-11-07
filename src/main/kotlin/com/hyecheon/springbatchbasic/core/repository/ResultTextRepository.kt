package com.hyecheon.springbatchbasic.core.repository

import com.hyecheon.springbatchbasic.core.domain.ResultText
import org.springframework.data.jpa.repository.JpaRepository

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/07
 */
interface ResultTextRepository : JpaRepository<ResultText, Long> {
}
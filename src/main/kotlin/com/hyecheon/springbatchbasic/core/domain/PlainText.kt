package com.hyecheon.springbatchbasic.core.domain

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/07
 */
@Entity
@DynamicUpdate
@Table(name = "plain_text")
class PlainText(text: String) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null

	@Column(nullable = false, name = "plain_text")
	var text: String = text
		private set

}
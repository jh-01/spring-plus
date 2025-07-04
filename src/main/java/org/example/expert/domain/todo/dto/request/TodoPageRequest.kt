package org.example.expert.domain.todo.dto.request

import java.time.LocalDateTime

data class TodoPageRequest (
    val page: Long = 1L,
    val size: Long = 10,
    val weather: String? = null,
    val since: LocalDateTime? = null,
    val until: LocalDateTime? = null
)
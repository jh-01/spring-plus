package org.example.expert.domain.todo.dto.request

import java.time.LocalDateTime

data class TodoPageRequest (
    val page: Int = 1,
    val size: Int = 10,
    val weather: String? = null,
    val since: LocalDateTime? = null,
    val until: LocalDateTime? = null
)
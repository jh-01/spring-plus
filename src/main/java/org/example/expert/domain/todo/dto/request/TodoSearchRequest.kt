package org.example.expert.domain.todo.dto.request

import java.time.LocalDateTime

data class TodoSearchRequest(
    val page: Int = 1,
    val size: Int = 10,
    val title: String? = null,
    val since: LocalDateTime? = null,
    val until: LocalDateTime? = null,
    val managerName: String? = null
)

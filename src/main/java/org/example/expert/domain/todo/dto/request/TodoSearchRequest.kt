package org.example.expert.domain.todo.dto.request

import java.time.LocalDateTime

data class TodoSearchRequest(
    val page: Long = 1L,
    val size: Long = 10,
    val title: String? = null,
    val since: LocalDateTime? = null,
    val until: LocalDateTime? = null,
    val managerName: String? = null
)

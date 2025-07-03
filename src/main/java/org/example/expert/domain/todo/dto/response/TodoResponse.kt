package org.example.expert.domain.todo.dto.response

import org.example.expert.domain.manager.dto.response.ManagerResponse
import org.example.expert.domain.user.dto.response.UserResponse
import java.time.LocalDateTime

data class TodoResponse (
    val id: Long?,
    val title: String?,
    val contents: String?,
    val weather: String?,
    val user: UserResponse?,
    val managers: List<ManagerResponse>?,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)
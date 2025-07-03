package org.example.expert.domain.todo.dto.response

import com.querydsl.core.annotations.QueryProjection

data class TodoSearchResponse @QueryProjection constructor(
    val id: Long?,
    val title: String?,
    val managerNum: Long?,
    val commentNum: Long?
)

package org.example.expert.domain.user.dto.response

import com.querydsl.core.annotations.QueryProjection


data class UserResponse @QueryProjection constructor(
    val id: Long?,
    val email: String?,
    val nickname: String?
)
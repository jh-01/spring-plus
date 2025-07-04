package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.dto.request.TodoSearchRequest
import org.example.expert.domain.todo.dto.response.TodoSearchResponse
import org.example.expert.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.util.Optional

interface QTodoRepository {
    fun findByIdWithUser(@Param("todoId") todoId: Long): Optional<Todo>
    fun findAll(pageable: Pageable): Page<Todo>
    fun findAllByOrderByModifiedAtDesc(pageable: Pageable, request: TodoSearchRequest): Page<TodoSearchResponse>
}
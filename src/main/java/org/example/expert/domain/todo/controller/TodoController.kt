package org.example.expert.domain.todo.controller

import jakarta.validation.Valid
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.todo.dto.request.TodoPageRequest
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.request.TodoSearchRequest
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.dto.response.TodoSearchResponse
import org.example.expert.domain.todo.service.TodoService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/todos")
class TodoController(
    val todoService: TodoService
) {
    // 생성
    @PostMapping
    fun saveTodo(@Valid @RequestBody todoSaveRequest: TodoSaveRequest) : ResponseEntity<TodoSaveResponse> {
        // 로그인 유저 정보 가져오기
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val authUser: AuthUser = authentication.principal as AuthUser

        return ResponseEntity.ok(todoService.saveTodo(authUser, todoSaveRequest))
    }

    // 다건 조회
    @GetMapping
    fun getTodos(@ModelAttribute todoPageRequest: TodoPageRequest) : ResponseEntity<Page<TodoResponse>?> {
        return ResponseEntity.ok(todoService.getTodos(todoPageRequest))
    }

    // 검색
    @GetMapping("/search")
    fun searchTodos(@ModelAttribute todoSearchRequest: TodoSearchRequest): ResponseEntity<Page<TodoSearchResponse>?> {
        return ResponseEntity.ok(todoService.searchTodos(todoSearchRequest))
    }

    // 단일 조회
    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoResponse> {
        return ResponseEntity.ok(todoService.getTodo(todoId))
    }
}
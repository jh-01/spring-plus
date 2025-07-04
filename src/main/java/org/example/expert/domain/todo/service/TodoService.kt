package org.example.expert.domain.todo.service

import lombok.extern.slf4j.Slf4j
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.manager.dto.response.ManagerResponse
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.manager.repository.ManagerRepository
import org.example.expert.domain.todo.dto.request.TodoPageRequest
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.request.TodoSearchRequest
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.dto.response.TodoSearchResponse
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.example.expert.domain.user.repository.UserRepository
import org.example.expert.global.client.WeatherClient
import org.example.expert.global.exception.CustomException
import org.example.expert.global.exception.ErrorType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Slf4j
@Service
class TodoService(
    val todoRepository: TodoRepository,
    val userRepository: UserRepository,
    val weatherClient: WeatherClient,
    val managerRepository: ManagerRepository
) {
    // Todo 저장
    fun saveTodo(authUser: AuthUser, todosaveRequest: TodoSaveRequest): TodoSaveResponse {
        // 인증 유저가 없는 상태 확인하고 예외처리
        if(authUser.id == null) {
            throw IllegalStateException("AuthUser ID is null")
        }

        // 해당 유저 조회
        val user: User = userRepository.findById(authUser.id)
            .orElseThrow() {
                IllegalStateException("User not found")
        }

        // 현재 날씨 정보 가져오기
        val weather: String = weatherClient.todayWeather

        // 받은 값으로 Todo 생성
        val todo: Todo = Todo(
            todosaveRequest.title,
            todosaveRequest.contents,
            weather,
            user
        )
        val savedTodo: Todo = todoRepository.save(todo)

        // 매니저 생성
        val manager: Manager = Manager(user, savedTodo)
        val savedManager: Manager = managerRepository.save(manager)

        return TodoSaveResponse(
            savedTodo.id,
            savedTodo.title,
            savedTodo.contents,
            weather,
            UserResponse(user.id, user.email, user.nickname),
            listOf(ManagerResponse(savedManager.id!!, UserResponse(savedManager.user.id, savedManager.user.email, savedManager.user.nickname)))
        )
    }

    // Todo 전체 페이징 조회
    fun getTodos(todoPageRequest: TodoPageRequest): Page<TodoResponse> {
        val pageable: Pageable = PageRequest.of((todoPageRequest.page - 1).toInt(), todoPageRequest.size.toInt())

        val todos : Page<Todo> = todoRepository.findAll(pageable)

        return todos.map { todo ->
            TodoResponse(
                todo.id!!,
                todo.title,
                todo.contents,
                todo.weather,
                UserResponse(
                    todo.user.id,
                    todo.user.email,
                    todo.user.nickname
                ),
                todo.managers.map { manager ->
                    ManagerResponse(
                        manager.id!!,
                        UserResponse(
                            manager.user.id,
                            manager.user.email,
                            manager.user.nickname
                        )
                    )
                },
                todo.createdAt,
                todo.modifiedAt
            )
        }
    }

    fun getTodo(todoId: Long): TodoResponse {
        val todo: Todo = todoRepository.findByIdWithUser(todoId)
            .orElseThrow() { CustomException(ErrorType.TODO_NOT_FOUND) }

        val user: User = todo.user

        return TodoResponse(
            todo.id,
            todo.title,
            todo.contents,
            todo.weather,
            UserResponse(user.id, user.email, user.nickname),
            todo.managers.map { manager ->
                ManagerResponse(
                    manager.id!!,
                    UserResponse(manager.id, manager.user.email, manager.user.nickname)
                )
            },
            todo.createdAt,
            todo.modifiedAt
        )
    }

    fun searchTodos(todoSearchRequest: TodoSearchRequest): Page<TodoSearchResponse> {
        val pageable: Pageable = PageRequest.of((todoSearchRequest.page - 1).toInt(), todoSearchRequest.size.toInt())

        return todoRepository.findAllByOrderByModifiedAtDesc(pageable, todoSearchRequest)
    }


}
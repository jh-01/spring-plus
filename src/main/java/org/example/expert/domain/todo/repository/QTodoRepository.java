package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QTodoRepository {
    public Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
    public Page<TodoSearchResponse> findAllByOrderByModifiedAtDesc(Pageable pageable, TodoSearchRequest request);
}

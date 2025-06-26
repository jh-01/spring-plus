package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QTodoRepository {
    public Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}

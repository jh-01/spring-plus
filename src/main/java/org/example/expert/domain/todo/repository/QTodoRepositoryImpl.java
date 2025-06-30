package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class QTodoRepositoryImpl implements QTodoRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId) {
        return queryFactory.selectFrom(todo)
                .distinct()
                .leftJoin(todo.user).fetchJoin()
                .leftJoin(todo.managers).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetch()
                .stream()
                .findFirst();
    }

    @Override
    public Page<TodoSearchResponse> findAllByOrderByModifiedAtDesc(Pageable pageable, TodoSearchRequest request) {
        List<TodoSearchResponse> searchResult = queryFactory.select(
                new QTodoSearchResponse(
                    todo.id,
                    todo.title,
                    manager.countDistinct(),
                    comment.countDistinct()
                )
        ).from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(
                        isSearchTitleExist(request.getTitle()),
                        isSearchManagerNameExist(request.getManagerName()),
                        isSinceDateExist(request.getSince()),
                        isUntilDateExist(request.getUntil())
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(todo.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(
                        isSearchTitleExist(request.getTitle()),
                        isSearchManagerNameExist(request.getManagerName()),
                        isSinceDateExist(request.getSince()),
                        isUntilDateExist(request.getUntil())
                )
                .fetchOne();

        return new PageImpl<>(searchResult, pageable, total != null ? total : 0);
    }

    private BooleanExpression isSearchTitleExist(String title) {
        return StringUtils.hasText(title)? todo.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression isSearchManagerNameExist(String managerName) {
        return StringUtils.hasText(managerName)
                ? manager.user.nickname.equalsIgnoreCase(managerName)
                : null;
    }

    private BooleanExpression isSinceDateExist(LocalDateTime since) {
        return since != null ? todo.createdAt.after(since) : null;
    }

    private BooleanExpression isUntilDateExist(LocalDateTime until) {
        return until != null ? todo.createdAt.before(until) : null;
    }
}


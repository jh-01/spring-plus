package org.example.expert.domain.todo.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.example.expert.domain.comment.entity.QComment.comment
import org.example.expert.domain.manager.entity.QManager.manager
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse
import org.example.expert.domain.todo.dto.request.TodoSearchRequest
import org.example.expert.domain.todo.dto.response.TodoSearchResponse
import org.example.expert.domain.todo.entity.QTodo.todo
import org.example.expert.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.StringUtils
import java.time.LocalDateTime
import java.util.*

class QTodoRepositoryImpl(
    val queryFactory: JPAQueryFactory
) : QTodoRepository {
    // id로 단건 조회
    override fun findByIdWithUser(todoId: Long): Optional<Todo> {
        return queryFactory.selectFrom(todo)
            .distinct() // 중복 방지
            .leftJoin(todo.user).fetchJoin() // fetchJoin을 사용해 N+1 문제 방지
            .leftJoin(todo.managers).fetchJoin()
            .where(todo.id.eq(todoId))
            .fetch()
            .stream()
            .findFirst(); // Optional로 리턴
    }

    // Todo 전체 목록 조회
    override fun findAll(pageable: Pageable): Page<Todo> {


        // 결과 조회
        val searchResult: MutableList<Todo?> = queryFactory
            .selectFrom(todo)
            .leftJoin(todo.managers, manager)
            .leftJoin(todo.comments, comment)
            .groupBy(todo.id) // 중복 방지를 위한 그룹화
            .orderBy(todo.createdAt.desc()) // 최신순으로 정렬
            .offset(pageable.offset.toLong())
            .limit(pageable.pageSize.toLong())
            .fetch()


        // 전체 개수 조회
        val total: Long? = queryFactory
            .select(todo.countDistinct()) // 중복 제거
            .from(todo)
            .leftJoin(todo.managers, manager)
            .leftJoin(todo.comments, comment)
            .fetchOne()

        return PageImpl(searchResult, pageable, if (total != null) total else 0)
    }

    // 조건 기반 검색
    // 제목, 매니저 이름, 날짜 범위 조건 추가 가능
    override fun findAllByOrderByModifiedAtDesc(
        pageable: Pageable,
        request: TodoSearchRequest
    ): Page<TodoSearchResponse> {
        val searchResult: List<TodoSearchResponse> = queryFactory.select(
            QTodoSearchResponse(
                todo.id,
                todo.title,
                manager.countDistinct(),
                comment.countDistinct()
            )
        ).from(todo)
            .leftJoin(todo.managers, manager)
            .leftJoin(todo.comments, comment)
            .where(
                isSearchTitleExist(request.title),  // 제목 조건
                isSearchManagerNameExist(request.managerName),  // 매니저 이름 조건
                isSinceDateExist(request.since),  // 시작 날짜 조건
                isUntilDateExist(request.until) // 종료 날짜 조건
            )
            .groupBy(todo.id)
            .orderBy(todo.createdAt.desc())
            .offset(pageable.offset.toLong())
            .limit(pageable.pageSize.toLong())
            .fetch()


        // 전체 개수 조회
        val total: Long? = queryFactory
            .select(todo.countDistinct())
            .from(todo)
            .leftJoin(todo.managers, manager)
            .leftJoin(todo.comments, comment)
            .where(
                isSearchTitleExist(request.title),
                isSearchManagerNameExist(request.managerName),
                isSinceDateExist(request.since),
                isUntilDateExist(request.until)
            )
            .fetchOne()

        return PageImpl(searchResult, pageable, if (total != null) total else 0)
    }


    /*
        조건 메서드 정의
     */
    // 제목 조건
    private fun isSearchTitleExist(title: String?): BooleanExpression? {
        return if (StringUtils.hasText(title)) todo.title.containsIgnoreCase(title) else null
    }

    // 매니저 이름 조건
    private fun isSearchManagerNameExist(managerName: String?): BooleanExpression? {
        return if (StringUtils.hasText(managerName))
            manager.user.nickname.equalsIgnoreCase(managerName)
        else
            null
    }

    // 날짜 조건
    private fun isSinceDateExist(since: LocalDateTime?): BooleanExpression? {
        return if (since != null) todo.createdAt.after(since) else null
    }

    private fun isUntilDateExist(until: LocalDateTime?): BooleanExpression? {
        return if (until != null) todo.createdAt.before(until) else null
    }
}
package org.example.expert.domain.manager.service

import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest
import org.example.expert.domain.manager.dto.response.ManagerResponse
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.manager.repository.ManagerRepository
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.example.expert.domain.user.repository.UserRepository
import org.example.expert.global.exception.CustomException
import org.example.expert.global.exception.ErrorType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.ObjectUtils
import java.util.function.Supplier

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ManagerService {
    private val managerRepository: ManagerRepository? = null
    private val userRepository: UserRepository? = null
    private val todoRepository: TodoRepository? = null
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun saveManager(authUser: AuthUser?, todoId: Long, managerSaveRequest: @Valid ManagerSaveRequest?): ManagerSaveResponse {
        // 일정을 만든 유저
        val user = User.fromAuthUser(authUser)
        val todo = todoRepository!!.findById(todoId)
            .orElseThrow<InvalidRequestException?>(Supplier { InvalidRequestException("Todo not found") })

        log.info("user: {}", user)
        log.info("userId: {}", user.getId())
        if (todo.user == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.user.getId())) {
            throw CustomException(ErrorType.NOT_USER_OF_TODO)
        }

        for (i in todo.managers.indices) {
            if (todo.managers[i].id == managerSaveRequest?.getManagerUserId()) {
                throw CustomException(ErrorType.INVALID_REQUEST)
            }
        }

        val managerUser = userRepository!!.findById(managerSaveRequest?.getManagerUserId())
            .orElseThrow<CustomException?>(Supplier { CustomException(ErrorType.USER_NOT_FOUND) })

        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            throw CustomException(ErrorType.OWNER_OF_TODO)
        }

        val newManagerUser = Manager(managerUser, todo)
        val savedManagerUser = managerRepository!!.save<Manager?>(newManagerUser)

        log.info("savedManagerUser: {}", savedManagerUser)

        return ManagerSaveResponse(
            savedManagerUser.getId(),
            UserResponse(managerUser.getId(), managerUser.getEmail(), user.getNickname())
        )
    }

    fun getManagers(todoId: Long): MutableList<ManagerResponse?> {
        val todo = todoRepository!!.findById(todoId)
            .orElseThrow<CustomException?>(Supplier { CustomException(ErrorType.TODO_NOT_FOUND) })

        val managerList = managerRepository!!.findByTodoIdWithUser(todo.id)

        val dtoList: MutableList<ManagerResponse?> = ArrayList<ManagerResponse?>()
        for (manager in managerList) {
            val user = manager.getUser()
            dtoList.add(
                ManagerResponse(
                    manager.getId(),
                    UserResponse(user.getId(), user.getEmail(), user.getNickname())
                )
            )
        }
        return dtoList
    }

    @Transactional
    fun deleteManager(authUser: AuthUser?, todoId: Long, managerId: Long) {
        val user = User.fromAuthUser(authUser)

        val todo = todoRepository!!.findById(todoId)
            .orElseThrow<CustomException?>(Supplier { CustomException(ErrorType.TODO_NOT_FOUND) })

        if (todo.user == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.user.getId())) {
            throw CustomException(ErrorType.HAVE_NO_AUTHORITY_OF_TODO)
        }

        val manager = managerRepository!!.findById(managerId)
            .orElseThrow<CustomException?>(Supplier { CustomException(ErrorType.MANAGER_NOT_FOUND) })

        if (!ObjectUtils.nullSafeEquals(todo.id, manager.getTodo().id)) {
            throw CustomException(ErrorType.HAVE_NO_AUTHORITY_OF_TODO)
        }

        managerRepository.delete(manager)
    }
}

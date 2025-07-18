package org.example.expert.domain.manager.controller

import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.log.entity.LogType
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest
import org.example.expert.domain.manager.dto.response.ManagerResponse
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse
import org.example.expert.domain.manager.service.ManagerService
import org.example.expert.global.log.annotation.LogWrite
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
class ManagerController {
    private val managerService: ManagerService
        get() {
            TODO()
        }

    @LogWrite(type = LogType.MANAGER_ADDED_TO_TODO)
    @PostMapping("/todos/{todoId}/managers")
    fun saveManager(
        @PathVariable todoId: Long,
        @RequestBody managerSaveRequest: @Valid ManagerSaveRequest?
    ): ResponseEntity<ManagerSaveResponse?> {
        val authentication = SecurityContextHolder.getContext().getAuthentication()
        val authUser = authentication.getPrincipal() as AuthUser?
        return ResponseEntity.ok<ManagerSaveResponse?>(
            managerService.saveManager(
                authUser,
                todoId,
                managerSaveRequest
            )
        )
    }

    @GetMapping("/todos/{todoId}/managers")
    fun getMembers(@PathVariable todoId: Long): ResponseEntity<MutableList<ManagerResponse?>?> {
        return ResponseEntity.ok<MutableList<ManagerResponse?>?>(managerService!!.getManagers(todoId))
    }

    @DeleteMapping("/todos/{todoId}/managers/{managerId}")
    fun deleteManager(
        @PathVariable todoId: Long,
        @PathVariable managerId: Long
    ) {
        val authentication = SecurityContextHolder.getContext().getAuthentication()
        val authUser = authentication.getPrincipal() as AuthUser?
        managerService.deleteManager(authUser, todoId, managerId)
    }
}

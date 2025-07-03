package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.user.dto.response.UserResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
public class TodoResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponse user;
    private final List<ManagerResponse> managers;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public TodoResponse(Long id, String title, String contents, String weather, UserResponse user, List<ManagerResponse> manager, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
        this.managers = manager == null ? Collections.emptyList() : manager;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

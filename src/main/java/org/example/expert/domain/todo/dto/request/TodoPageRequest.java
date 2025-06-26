package org.example.expert.domain.todo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TodoPageRequest {
    private int page = 1;
    private int size = 10;
    private String weather;
    private LocalDateTime since;
    private LocalDateTime until;
}

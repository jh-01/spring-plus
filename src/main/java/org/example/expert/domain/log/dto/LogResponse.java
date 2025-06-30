package org.example.expert.domain.log.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.entity.LogType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LogResponse {

    private final int id;
    private final int userId;
    private final int targetId;
    private final LogType type;
    private final String method;

    private final String url;

    private final String status;
    private final String errorMessage;

    private final String ipAddress;
    private final LocalDateTime createAt;


    public static LogResponse toDto(Log log) {
        return new LogResponse(Math.toIntExact(log.getId()), Math.toIntExact(log.getUserId()), Math.toIntExact(log.getTargetId()),log.getType(), log.getMethod(), log.getUrl(), log.getStatus(), log.getErrorMessage(), log.getIpAddress(),log.getCreatedAt());
    }
}

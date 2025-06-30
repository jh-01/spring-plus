package org.example.expert.domain.log.service;


import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.dto.LogResponse;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.entity.LogType;
import org.example.expert.domain.log.repository.LogRepository;
import org.example.expert.global.exception.CustomException;
import org.example.expert.global.exception.ErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public Page<LogResponse> getLogs(LogType type, Pageable pageable, LocalDateTime startDate, LocalDateTime endDate) {
        Page<Log> logPage;
        if ( type == LogType.ALL ){
            logPage = logRepository.findByCreatedAtBetween(startDate,endDate,pageable);
        }
        else{
            logPage = logRepository.findByTypeAndCreatedAtBetween(type,startDate,endDate,pageable);
        }

        return logPage.map(LogResponse::toDto);
    }

    public LogResponse getLog(int logId) {
        Optional<Log> logOpt = logRepository.findById((long) logId);
        if ( logOpt.isEmpty()) {
            throw new CustomException(ErrorType.LOG_NOT_FOUND);
        }

        return LogResponse.toDto(logOpt.get());
    }
}

package org.example.expert.domain.log.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.dto.LogResponse;
import org.example.expert.domain.log.entity.LogType;
import org.example.expert.domain.log.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping("/logs")
    public ResponseEntity<Page<LogResponse>> getLog(@RequestParam(defaultValue = "ALL") LogType type,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size,
                                                    @RequestParam(defaultValue = "time") String sortType,
                                                    @RequestParam(defaultValue = "desc") String direction,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        if (endDate == null) {
            endDate = now;
        }
        if (startDate == null) {
            startDate = endDate.minusDays(7);
        }

        String sortField = sortType.equalsIgnoreCase("type") ? "type" : "createdAt";
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.getLogs(type, PageRequest.of(page,size,Sort.by(sortDirection,sortField)), startDate, endDate));
    }

    @GetMapping("/logs/{logId}")
    public ResponseEntity<LogResponse> getLog(@PathVariable int logId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.getLog(logId));
    }
}

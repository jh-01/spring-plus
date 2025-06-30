package org.example.expert.domain.log.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.entity.LogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Log> findByTypeAndCreatedAtBetween(LogType type, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

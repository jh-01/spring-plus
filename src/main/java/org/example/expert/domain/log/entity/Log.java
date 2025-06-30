package org.example.expert.domain.log.entity;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LogType type;
    @Column(nullable = false)
    private String method;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String status;

    @Setter
    private String errorMessage;

    @Column(nullable = false)
    private String ipAddress;

    @CreatedDate
    private LocalDateTime createdAt;

    @QueryProjection
    public Log(Long targetId, Long userId, LogType type, String method, String url, String status, String ipAddress, LocalDateTime createdAt) {
        this.targetId = targetId;
        this.userId = userId;
        this.type = type;
        this.method = method;
        this.url = url;
        this.status = status;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

}

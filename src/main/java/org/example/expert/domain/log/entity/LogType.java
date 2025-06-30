package org.example.expert.domain.log.entity;

import lombok.Getter;

@Getter
public enum LogType {
    ALL(IdSource.NULL,IdSource.NULL,""),
    // NOTE : userId 요청자 ID
    // NOTE : targetId 대상 ID ( 테스크, 댓글, 로그인, 로그아웃 )
     /*
     userId = { token }
     targetId = { response: { generatorId } }
      */

    MANAGER_ADDED_TO_TODO(IdSource.TOKEN, IdSource.RESPONSE, "targetId"),
    MANAGER_ADD_FAIL(IdSource.TOKEN, IdSource.RESPONSE, "targetId");

    private final IdSource userSource;
    private final IdSource targetSource;
    private final String targetKey;

    LogType(IdSource userSource, IdSource targetSource, String targetKey) {
        this.userSource = userSource;
        this.targetSource = targetSource;
        this.targetKey = targetKey;
    }

}

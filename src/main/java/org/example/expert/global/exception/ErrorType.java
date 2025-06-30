package org.example.expert.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    /**
     * 200 OK: 성공
     * 400 Bad Request: 잘못된 요청 (유효성 검사 실패)
     * 401 Unauthorized: 인증 실패
     * 403 Forbidden: 권한 없음
     * 404 Not Found: 리소스 없음
     * 406 CONFLICT: 요청이 서버의 현재 상태와 충돌
     * 500 Internal Server Error: 서버 오류
     */

    // 공통 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "처리 중 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰 입니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 서명입니다."),

    // 유저 관련 에러
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 사용자명 또는 비밀번호입니다."),
    PASSWORD_SAME(HttpStatus.UNAUTHORIZED, "기존 비밀번호와 새로운 비밀번호가 동일합니다."),

    // Todo 관련 에러
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."),

    // 매니저 관련 에러
    DUPLICATE_MANAGER(HttpStatus.CONFLICT, "이미 등록된 매니저입니다."),
    NOT_USER_OF_TODO(HttpStatus.UNAUTHORIZED, "담당자를 등록하려고 하는 유저가 유효하지 않거나, 일정을 만든 유저가 아닙니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "등록하려고 하는 담당자 유저가 존재하지 않습니다."),
    OWNER_OF_TODO(HttpStatus.BAD_REQUEST, "일정 작성자는 본인을 담당자로 등록할 수 없습니다."),
    HAVE_NO_AUTHORITY_OF_TODO(HttpStatus.UNAUTHORIZED, "매니저 등록 및 삭제는 매니저만 할 수 있습니다."),
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는ㄴ 매니저입니다."),


    // 로그 관련 에러
    LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 로그를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}

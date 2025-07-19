package cotato.timetile.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResponse {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON003", "올바르지 않은 입력"),

    INVALID_USER(HttpStatus.UNAUTHORIZED, "AUTH001", "존재하지 않는 유저"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH002", "유효하지 않은 토큰"),

    INVALID_PROVIDER(HttpStatus.FORBIDDEN, "AUTH003", "유효하지 않은 로그인 방식"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH004", "접근 권한 없음"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON001", "요청 자원 없음"),

    ALREADY_PROCESSED(HttpStatus.CONFLICT, "COMMON004", "이미 처리됨"),

    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "COMMON002", "요청 가능 횟수 초과"),

    INVALID_FILE(HttpStatus.UNPROCESSABLE_ENTITY, "COMMON005", "올바르지 않은 파일"),

    FAILED_FILE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER001", "파일 업로드 실패"),
    TIMEOUT_EXTERNAL_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER002", "외부 통신 실패"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}

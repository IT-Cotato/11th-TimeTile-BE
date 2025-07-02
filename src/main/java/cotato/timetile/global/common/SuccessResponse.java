package cotato.timetile.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessResponse {
    OK(HttpStatus.OK, "COMMON001", "요청 성공"),
    CREATED(HttpStatus.CREATED, "COMMON002", "생성 완료");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

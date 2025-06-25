package cotato.timetile.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessResponse {
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}

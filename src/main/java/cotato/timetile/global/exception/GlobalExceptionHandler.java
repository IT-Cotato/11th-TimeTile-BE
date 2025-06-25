package cotato.timetile.global.exception;

import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<?>> handleCustomException(CustomException e) {
        return ApiResponseUtil.error(e.getErrorResponse());
    }
}

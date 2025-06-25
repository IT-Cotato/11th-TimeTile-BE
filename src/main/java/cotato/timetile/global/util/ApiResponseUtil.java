package cotato.timetile.global.util;

import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.ErrorResponse;
import cotato.timetile.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {
    public static ResponseEntity<CommonResponse<?>> success(SuccessResponse successResponse) {
        return ResponseEntity.status(successResponse.getStatus()).body(CommonResponse.success(successResponse));
    }

    public static <T> ResponseEntity<CommonResponse<?>> success(SuccessResponse successResponse, T data) {
        return ResponseEntity.status(successResponse.getStatus()).body(CommonResponse.success(successResponse, data));
    }

    public static ResponseEntity<CommonResponse<?>> error(ErrorResponse errorResponse) {
        return ResponseEntity.status(errorResponse.getStatus()).body(CommonResponse.error(errorResponse));
    }
}

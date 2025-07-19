package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class ForbiddenException extends CustomException {

    public ForbiddenException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static ForbiddenException wrong() {
        return new ForbiddenException(ErrorResponse.ACCESS_DENIED);
    }

}

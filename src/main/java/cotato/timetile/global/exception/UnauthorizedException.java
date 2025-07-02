package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static UnauthorizedException failed() {
        return new UnauthorizedException(ErrorResponse.INVALID_USER);
    }

    public static UnauthorizedException invalid() {
        return new UnauthorizedException(ErrorResponse.INVALID_TOKEN);
    }

}

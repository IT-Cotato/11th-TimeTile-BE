package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class BadRequestException extends CustomException {

    public BadRequestException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static BadRequestException wrong() {
        return new BadRequestException(ErrorResponse.INVALID_INPUT);
    }

}

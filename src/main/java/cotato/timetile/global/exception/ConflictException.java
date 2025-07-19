package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class ConflictException extends CustomException {

    public ConflictException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static ConflictException wrong() {
        return new ConflictException(ErrorResponse.ALREADY_PROCESSED);
    }

}

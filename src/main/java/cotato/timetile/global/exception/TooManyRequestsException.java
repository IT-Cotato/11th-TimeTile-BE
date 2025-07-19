package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class TooManyRequestsException extends CustomException {

    public TooManyRequestsException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static TooManyRequestsException wrong() {
        return new TooManyRequestsException(ErrorResponse.TOO_MANY_REQUESTS);
    }

}

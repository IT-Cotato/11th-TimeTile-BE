package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class NotFoundException extends CustomException {

    public NotFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static NotFoundException wrong() {
        return new NotFoundException(ErrorResponse.NOT_FOUND);
    }

}

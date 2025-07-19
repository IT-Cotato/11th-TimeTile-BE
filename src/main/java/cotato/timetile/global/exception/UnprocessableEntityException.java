package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class UnprocessableEntityException extends CustomException {

    public UnprocessableEntityException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static UnprocessableEntityException invalid() {
        return new UnprocessableEntityException(ErrorResponse.INVALID_FILE);
    }

}

package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class NotFoundException extends CustomException {

    public NotFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
    
}

package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class IOException extends CustomException {

    public IOException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static IOException upload() {
        return new IOException(ErrorResponse.FAILED_FILE_UPLOAD);
    }

}

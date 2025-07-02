package cotato.timetile.global.exception;

import cotato.timetile.global.common.ErrorResponse;

public class AuthenticationProviderException extends CustomException {

    public AuthenticationProviderException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public static AuthenticationProviderException invalid() {
        return new AuthenticationProviderException(ErrorResponse.INVALID_PROVIDER);
    }
    
}

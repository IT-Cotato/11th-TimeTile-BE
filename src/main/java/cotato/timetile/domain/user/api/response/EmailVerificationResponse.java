package cotato.timetile.domain.user.api.response;

public record EmailVerificationResponse(
        boolean isValid
) {
    public static EmailVerificationResponse of(boolean isValid) {
        return new EmailVerificationResponse(isValid);
    }
}

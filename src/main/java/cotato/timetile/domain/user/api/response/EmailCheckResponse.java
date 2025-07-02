package cotato.timetile.domain.user.api.response;

public record EmailCheckResponse(
        boolean isAvailable
) {
    public static EmailCheckResponse of(boolean isAvailable) {
        return new EmailCheckResponse(isAvailable);
    }
}

package cotato.timetile.domain.user.api.response;

public record NicknameCheckResponse(
        boolean isAvailable
) {
    public static NicknameCheckResponse of(boolean isAvailable) {
        return new NicknameCheckResponse(isAvailable);
    }
}

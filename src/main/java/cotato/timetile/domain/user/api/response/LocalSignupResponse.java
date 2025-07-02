package cotato.timetile.domain.user.api.response;

public record LocalSignupResponse(
        String accessToken,
        String refreshToken
) {
    public static LocalSignupResponse of(String accessToken, String refreshToken) {
        return new LocalSignupResponse(accessToken, refreshToken);
    }
}

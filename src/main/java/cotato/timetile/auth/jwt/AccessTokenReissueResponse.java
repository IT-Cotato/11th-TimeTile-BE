package cotato.timetile.auth.jwt;

public record AccessTokenReissueResponse(
        String accessToken
) {
    public static AccessTokenReissueResponse of(String accessToken) {
        return new AccessTokenReissueResponse(accessToken);
    }
}

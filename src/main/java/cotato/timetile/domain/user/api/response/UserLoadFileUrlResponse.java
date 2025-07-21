package cotato.timetile.domain.user.api.response;

public record UserLoadFileUrlResponse(
        String key,
        String url
) {
    public static UserLoadFileUrlResponse of(String key, String url) {
        return new UserLoadFileUrlResponse(key, url);
    }
}

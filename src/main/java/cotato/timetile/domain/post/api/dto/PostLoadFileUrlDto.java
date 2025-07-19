package cotato.timetile.domain.post.api.dto;

public record PostLoadFileUrlDto(
        String key,
        String url
) {
    public static PostLoadFileUrlDto of(String key, String url) {
        return new PostLoadFileUrlDto(key, url);
    }
}

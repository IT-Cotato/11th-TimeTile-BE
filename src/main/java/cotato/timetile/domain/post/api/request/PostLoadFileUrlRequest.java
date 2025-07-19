package cotato.timetile.domain.post.api.request;

import java.util.List;

public record PostLoadFileUrlRequest(
        List<String> extensions
) {
    public static PostLoadFileUrlRequest of(List<String> extensions) {
        return new PostLoadFileUrlRequest(extensions);
    }
}

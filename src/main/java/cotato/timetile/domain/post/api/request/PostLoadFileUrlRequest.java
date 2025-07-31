package cotato.timetile.domain.post.api.request;

import cotato.timetile.annotation.Media;
import java.util.List;

public record PostLoadFileUrlRequest(
        @Media List<String> extensions
) {
    public static PostLoadFileUrlRequest of(List<String> extensions) {
        return new PostLoadFileUrlRequest(extensions);
    }
}

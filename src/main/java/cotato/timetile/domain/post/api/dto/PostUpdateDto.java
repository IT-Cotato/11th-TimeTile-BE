package cotato.timetile.domain.post.api.dto;

import cotato.timetile.domain.post.api.request.PostUpdateRequest;
import cotato.timetile.global.common.Visibility;
import java.util.List;

public record PostUpdateDto(
        String title,
        String content,
        Visibility visibility,
        List<String> mediaKeys,
        String mainImageKey
) {
    public static PostUpdateDto of(PostUpdateRequest request, String mainImageKey) {
        return new PostUpdateDto(
                request.title(),
                request.content(),
                request.visibility(),
                request.mediaKeys(),
                mainImageKey
        );
    }
}

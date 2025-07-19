package cotato.timetile.domain.post.api.dto;

import cotato.timetile.domain.post.api.request.PostCreationRequest;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.global.common.Visibility;
import java.time.LocalDate;
import java.util.List;

public record PostCreationDto(
        String groupId,
        String title,
        String content,
        Visibility visibility,
        List<String> mediaKeys,
        String mainImageKey,
        LocalDate startedAt,
        User author
) {
    public static PostCreationDto of(PostCreationRequest request, String mainImageKey,
                                     LocalDate startedAt, User author) {
        return new PostCreationDto(
                request.groupId(),
                request.title(),
                request.content(),
                request.visibility(),
                request.mediaKeys(),
                mainImageKey,
                startedAt,
                author
        );
    }
}

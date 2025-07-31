package cotato.timetile.domain.profile.api.response;

import cotato.timetile.domain.profile.api.dto.PostLoadAllOnProfileDto;
import java.util.List;

public record PostLoadAllOnLimitResponse(
        List<PostLoadAllOnProfileDto> posts
) {
    public static PostLoadAllOnLimitResponse of(List<PostLoadAllOnProfileDto> posts) {
        return new PostLoadAllOnLimitResponse(posts);
    }
}

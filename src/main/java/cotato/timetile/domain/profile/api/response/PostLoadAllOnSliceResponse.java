package cotato.timetile.domain.profile.api.response;

import cotato.timetile.domain.profile.api.dto.PostLoadAllOnProfileDto;
import java.util.List;

public record PostLoadAllOnSliceResponse(
        List<PostLoadAllOnProfileDto> posts,
        boolean hasNext,
        Long lastPostId
) {
    public static PostLoadAllOnSliceResponse of(List<PostLoadAllOnProfileDto> posts,
                                                boolean hasNext,
                                                Long lastPostId) {
        return new PostLoadAllOnSliceResponse(posts, hasNext, lastPostId);
    }
}

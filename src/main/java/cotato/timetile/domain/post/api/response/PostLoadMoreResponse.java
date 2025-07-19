package cotato.timetile.domain.post.api.response;

import cotato.timetile.domain.post.api.dto.PostLoadMoreDto;
import java.util.List;

public record PostLoadMoreResponse(
        List<PostLoadMoreDto> posts,
        boolean hasNext,
        Long lastPostId
) {
    public static PostLoadMoreResponse of(List<PostLoadMoreDto> posts, boolean hasNext, Long lastPostId) {
        return new PostLoadMoreResponse(posts, hasNext, lastPostId);
    }
}

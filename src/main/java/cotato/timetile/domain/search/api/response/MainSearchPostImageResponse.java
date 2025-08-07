package cotato.timetile.domain.search.api.response;

import cotato.timetile.domain.search.api.dto.MainSearchPostImageDto;
import java.util.List;

public record MainSearchPostImageResponse(
        List<MainSearchPostImageDto> posts,
        boolean hasNext,
        Long lastPostId
) {
    public static MainSearchPostImageResponse of(List<MainSearchPostImageDto> posts,
                                                 boolean hasNext,
                                                 Long lastPostId) {
        return new MainSearchPostImageResponse(posts, hasNext, lastPostId);
    }
}

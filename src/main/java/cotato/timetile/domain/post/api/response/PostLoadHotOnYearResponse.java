package cotato.timetile.domain.post.api.response;

import cotato.timetile.domain.post.api.dto.PostLoadHotOnYearDto;
import java.util.List;
import java.util.Map;

public record PostLoadHotOnYearResponse(
        Map<Integer, List<PostLoadHotOnYearDto>> posts
) {
    public static PostLoadHotOnYearResponse of(Map<Integer, List<PostLoadHotOnYearDto>> posts) {
        return new PostLoadHotOnYearResponse(posts);
    }
}

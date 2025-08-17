package cotato.timetile.domain.home.api.response;

import cotato.timetile.domain.home.api.dto.PostLoadOnHomeDto;
import java.util.List;

public record PostLoadOnHomeResponse(
        List<PostLoadOnHomeDto> posts
) {
    public static PostLoadOnHomeResponse of(List<PostLoadOnHomeDto> posts) {
        return new PostLoadOnHomeResponse(posts);
    }
}

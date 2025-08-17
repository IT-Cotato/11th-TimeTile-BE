package cotato.timetile.domain.home.api.response;

import cotato.timetile.domain.home.api.dto.FollowPostLoadOnHomeDto;
import cotato.timetile.domain.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.PageImpl;

public record FollowPostLoadOnHomeResponse(
        List<FollowPostLoadOnHomeDto> posts,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static FollowPostLoadOnHomeResponse of(List<FollowPostLoadOnHomeDto> posts, PageImpl<Post> postPage) {
        return new FollowPostLoadOnHomeResponse(
                posts,
                postPage.hasNext(),
                postPage.hasPrevious(),
                postPage.isLast()
        );
    }
}

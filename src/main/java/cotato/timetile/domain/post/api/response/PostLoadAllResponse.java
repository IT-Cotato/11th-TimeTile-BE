package cotato.timetile.domain.post.api.response;

import cotato.timetile.domain.post.api.dto.PostLoadAllDto;
import cotato.timetile.domain.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.PageImpl;

public record PostLoadAllResponse(
        List<PostLoadAllDto> posts,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static PostLoadAllResponse of(List<PostLoadAllDto> posts, PageImpl<Post> postPage) {
        return new PostLoadAllResponse(
                posts,
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalPages(),
                postPage.getTotalElements(),
                postPage.hasNext(),
                postPage.hasPrevious(),
                postPage.isLast()
        );
    }
}

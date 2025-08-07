package cotato.timetile.domain.search.api.response;

import cotato.timetile.domain.post.domain.PostDocument;
import cotato.timetile.domain.search.api.dto.MainSearchPostDto;
import java.util.List;
import org.springframework.data.domain.Page;

public record MainSearchPostResponse(
        List<MainSearchPostDto> posts,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static MainSearchPostResponse of(List<MainSearchPostDto> posts, Page<PostDocument> postPage) {
        return new MainSearchPostResponse(
                posts,
                postPage.getNumber() + 1,
                postPage.getSize(),
                postPage.getTotalPages(),
                postPage.getTotalElements(),
                postPage.hasNext(),
                postPage.hasPrevious(),
                postPage.isLast()
        );
    }
}

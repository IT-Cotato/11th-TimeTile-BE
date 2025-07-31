package cotato.timetile.domain.profile.api.response;

import com.querydsl.core.Tuple;
import cotato.timetile.domain.profile.api.dto.PostLoadAllOnProfileDto;
import java.util.List;
import org.springframework.data.domain.PageImpl;

public record PostLoadAllOnPageResponse(
        List<PostLoadAllOnProfileDto> posts,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static PostLoadAllOnPageResponse of(List<PostLoadAllOnProfileDto> posts, PageImpl<Tuple> postPage) {
        return new PostLoadAllOnPageResponse(
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

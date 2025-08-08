package cotato.timetile.domain.profile.api.response;

import com.querydsl.core.Tuple;
import cotato.timetile.domain.profile.api.dto.PostWithAuthorLoadAllOnProfileDto;
import java.util.List;
import org.springframework.data.domain.PageImpl;

public record PostWithAuthorLoadAllOnPageResponse(
        List<PostWithAuthorLoadAllOnProfileDto> posts,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static PostWithAuthorLoadAllOnPageResponse of(List<PostWithAuthorLoadAllOnProfileDto> posts,
                                                         PageImpl<Tuple> postPage) {
        return new PostWithAuthorLoadAllOnPageResponse(
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

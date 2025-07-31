package cotato.timetile.domain.profile.api.response;

import com.querydsl.core.Tuple;
import cotato.timetile.domain.profile.api.dto.CommentLoadAllOnProfileDto;
import java.util.List;
import org.springframework.data.domain.PageImpl;

public record CommentLoadAllOnProfileResponse(
        List<CommentLoadAllOnProfileDto> comments,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static CommentLoadAllOnProfileResponse of(List<CommentLoadAllOnProfileDto> comments,
                                                     PageImpl<Tuple> commentPage) {
        return new CommentLoadAllOnProfileResponse(
                comments,
                commentPage.getNumber() + 1,
                commentPage.getSize(),
                commentPage.getTotalPages(),
                commentPage.getTotalElements(),
                commentPage.hasNext(),
                commentPage.hasPrevious(),
                commentPage.isLast()
        );
    }
}

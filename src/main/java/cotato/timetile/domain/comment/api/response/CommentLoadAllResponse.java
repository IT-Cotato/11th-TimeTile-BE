package cotato.timetile.domain.comment.api.response;

import cotato.timetile.domain.comment.api.dto.CommentLoadAllDto;
import java.util.List;

public record CommentLoadAllResponse(
        List<CommentLoadAllDto> comments
) {
    public static CommentLoadAllResponse of(List<CommentLoadAllDto> comments) {
        return new CommentLoadAllResponse(comments);
    }
}

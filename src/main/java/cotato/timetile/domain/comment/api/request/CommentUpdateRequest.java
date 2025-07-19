package cotato.timetile.domain.comment.api.request;

import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        @Size(max = 300, min = 1) String content
) {
}

package cotato.timetile.domain.comment.api.request;

import jakarta.validation.constraints.NotBlank;

public record CommentCreationRequest(
        Long parentId,
        @NotBlank String content
) {
}

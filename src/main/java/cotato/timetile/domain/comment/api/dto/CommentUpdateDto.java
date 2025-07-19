package cotato.timetile.domain.comment.api.dto;

import cotato.timetile.domain.comment.api.request.CommentUpdateRequest;

public record CommentUpdateDto(
        String content
) {
    public static CommentUpdateDto of(CommentUpdateRequest request) {
        return new CommentUpdateDto(request.content());
    }
}

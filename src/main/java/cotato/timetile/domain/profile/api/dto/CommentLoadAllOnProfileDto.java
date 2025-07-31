package cotato.timetile.domain.profile.api.dto;

import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.event.domain.Event;

public record CommentLoadAllOnProfileDto(
        String name,
        String artistName,
        Long postId,
        String postTitle,
        String content,
        int likeCount
) {
    public static CommentLoadAllOnProfileDto of(Comment comment, Event event) {
        return new CommentLoadAllOnProfileDto(
                event.getName(),
                event.getArtist().getName(),
                comment.getPost().getId(),
                comment.getPost().getTitle(),
                comment.getContent(),
                comment.getLikeCount()
        );
    }
}

package cotato.timetile.domain.comment.api.dto;

import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.user.domain.User;

public record CommentCreationDto(
        User commenter,
        Post post,
        Comment parent,
        String content
) {
    public static CommentCreationDto of(User commenter, Post post, Comment parent, String content) {
        return new CommentCreationDto(commenter, post, parent, content);
    }
}

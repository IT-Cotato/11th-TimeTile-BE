package cotato.timetile.domain.comment.api.dto;

import cotato.timetile.domain.comment.domain.Comment;
import java.time.LocalDateTime;
import java.util.List;

public record CommentLoadAllDto(
        Long commenterId,
        String commenterNickname,
        String commenterProfileImageUrl,
        Long commentId,
        String content,
        LocalDateTime updatedAt,
        int likeCount,
        List<ReplyDto> replies
) {
    public static CommentLoadAllDto of(Comment comment, String commenterProfileImageUrl, List<ReplyDto> replies) {
        return new CommentLoadAllDto(
                comment.getCommenter().getId(),
                comment.getCommenter().getNickname(),
                commenterProfileImageUrl,
                comment.getId(),
                comment.getContent(),
                comment.getTimeInfo().getUpdatedAt(),
                comment.getLikeCount(),
                replies
        );
    }
}

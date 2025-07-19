package cotato.timetile.domain.comment.api.dto;

import cotato.timetile.domain.comment.domain.Comment;
import java.time.LocalDateTime;

public record ReplyDto(
        Long replyId,
        String content,
        Long replierId,
        String replierNickname,
        String replierProfileImageUrl,
        LocalDateTime updatedAt,
        int likeCount
) {
    public static ReplyDto of(Comment comment, String replierProfileImageUrl) {
        return new ReplyDto(
                comment.getId(),
                comment.getContent(),
                comment.getCommenter().getId(),
                comment.getCommenter().getNickname(),
                replierProfileImageUrl,
                comment.getTimeInfo().getUpdatedAt(),
                comment.getLikeCount()
        );
    }
}

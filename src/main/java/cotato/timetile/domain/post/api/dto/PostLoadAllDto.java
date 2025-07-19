package cotato.timetile.domain.post.api.dto;

import cotato.timetile.domain.post.domain.Post;
import java.time.LocalDateTime;

public record PostLoadAllDto(
        Long postId,
        String groupId,
        String title,
        String content,
        String mainImageUrl,
        LocalDateTime createdAt,
        int likeCount,
        int commentCount,
        Long authorId,
        String authorNickname,
        String authorProfileImageUrl
) {
    public static PostLoadAllDto of(Post post, String mainImageUrl, String authorProfileImageUrl) {
        return new PostLoadAllDto(
                post.getId(),
                post.getGroupId(),
                post.getTitle(),
                post.getContent(),
                mainImageUrl,
                post.getTimeInfo().getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getAuthor().getId(),
                post.getAuthor().getNickname(),
                authorProfileImageUrl
        );
    }
}

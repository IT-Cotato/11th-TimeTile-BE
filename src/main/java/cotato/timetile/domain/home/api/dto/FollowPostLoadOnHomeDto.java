package cotato.timetile.domain.home.api.dto;

import cotato.timetile.domain.post.domain.Post;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FollowPostLoadOnHomeDto(
        Long postId,
        String groupId,
        String title,
        String content,
        String mainImageUrl,
        LocalDate startedAt,
        LocalDateTime createdAt,
        int likeCount,
        int commentCount,
        Long authorId,
        String authorNickname,
        String authorProfileImageUrl
) {
    public static FollowPostLoadOnHomeDto of(Post post, String mainImageUrl, String authorProfileImageUrl) {
        return new FollowPostLoadOnHomeDto(
                post.getId(),
                post.getGroupId(),
                post.getTitle(),
                post.getContent(),
                mainImageUrl,
                post.getStartedAt(),
                post.getTimeInfo().getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getAuthor().getId(),
                post.getAuthor().getNickname(),
                authorProfileImageUrl
        );
    }
}

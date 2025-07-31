package cotato.timetile.domain.profile.api.dto;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.post.domain.Post;
import java.time.LocalDateTime;

public record PostLoadAllOnProfileDto(
        String name,
        String groupId,
        String artistName,
        Long postId,
        String title,
        String content,
        String mainImageUrl,
        LocalDateTime createdAt,
        int likeCount,
        int commentCount
) {
    public static PostLoadAllOnProfileDto of(Post post, String mainImageUrl, Event event) {
        return new PostLoadAllOnProfileDto(
                event.getName(),
                post.getGroupId(),
                event.getArtist().getName(),
                post.getId(),
                post.getTitle(),
                post.getContent(),
                mainImageUrl,
                post.getTimeInfo().getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount()
        );
    }
}

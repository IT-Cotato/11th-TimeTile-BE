package cotato.timetile.domain.profile.api.dto;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.post.domain.Post;
import java.time.LocalDateTime;

public record PostWithAuthorLoadAllOnProfileDto(
        String name,
        String groupId,
        String artistName,
        Long postId,
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
    public static PostWithAuthorLoadAllOnProfileDto of(Post post, String mainImageUrl, Event event,
                                                       String authorProfileImageUrl) {
        return new PostWithAuthorLoadAllOnProfileDto(
                event.getName(),
                post.getGroupId(),
                event.getArtist().getName(),
                post.getId(),
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

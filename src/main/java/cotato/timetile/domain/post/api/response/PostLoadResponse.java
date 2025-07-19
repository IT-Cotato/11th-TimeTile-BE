package cotato.timetile.domain.post.api.response;

import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.user.domain.Role;
import cotato.timetile.global.common.Visibility;
import java.time.LocalDateTime;
import java.util.List;

public record PostLoadResponse(
        Long postId,
        String title,
        String content,
        List<String> mediaUrls,
        LocalDateTime createdAt,
        Visibility visibility,
        int likeCount,
        int commentCount,
        int scrapCount,
        Long authorId,
        String authorNickname,
        String authorProfileImageUrl,
        Role authorRole
) {
    public static PostLoadResponse of(Post post, String authorProfileImageUrl, List<String> mediaUrls) {
        return new PostLoadResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                mediaUrls,
                post.getTimeInfo().getCreatedAt(),
                post.getVisibility(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getScrapCount(),
                post.getAuthor().getId(),
                post.getAuthor().getNickname(),
                authorProfileImageUrl,
                post.getAuthor().getRole()
        );
    }
}

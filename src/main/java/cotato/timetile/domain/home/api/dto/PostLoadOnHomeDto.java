package cotato.timetile.domain.home.api.dto;

import cotato.timetile.domain.post.domain.Post;
import java.time.LocalDateTime;

public record PostLoadOnHomeDto(
        String artistName,
        Long id,
        String title,
        LocalDateTime editedAt
) {
    public static PostLoadOnHomeDto of(Post post) {
        return new PostLoadOnHomeDto(
                post.getArtist().getName(),
                post.getId(),
                post.getTitle(),
                post.getTimeInfo().getCreatedAt()
        );
    }
}

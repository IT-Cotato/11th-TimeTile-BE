package cotato.timetile.domain.search.api.dto;

import cotato.timetile.domain.post.domain.Post;

public record MainSearchPostImageDto(
        Long id,
        String title,
        String imageUrl
) {
    public static MainSearchPostImageDto of(Post post, String imageUrl) {
        return new MainSearchPostImageDto(post.getId(), post.getTitle(), imageUrl);
    }
}

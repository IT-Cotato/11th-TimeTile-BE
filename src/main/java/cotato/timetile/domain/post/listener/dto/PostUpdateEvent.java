package cotato.timetile.domain.post.listener.dto;

import cotato.timetile.domain.post.domain.Post;

public record PostUpdateEvent(
        Long id
) {
    public static PostUpdateEvent of(Post post) {
        return new PostUpdateEvent(post.getId());
    }
}

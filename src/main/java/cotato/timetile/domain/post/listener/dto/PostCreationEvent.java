package cotato.timetile.domain.post.listener.dto;

import cotato.timetile.domain.post.domain.Post;

public record PostCreationEvent(
        Long id
) {
    public static PostCreationEvent of(Post post) {
        return new PostCreationEvent(post.getId());
    }
}

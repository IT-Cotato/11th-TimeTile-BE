package cotato.timetile.domain.post.listener.dto;

import cotato.timetile.domain.post.domain.Post;

public record PostRemovalEvent(
        String id
) {
    public static PostRemovalEvent of(Post post) {
        return new PostRemovalEvent(post.getId().toString());
    }
}

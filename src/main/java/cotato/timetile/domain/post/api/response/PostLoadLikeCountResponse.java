package cotato.timetile.domain.post.api.response;

public record PostLoadLikeCountResponse(
        int likeCount
) {
    public static PostLoadLikeCountResponse of(int likeCount) {
        return new PostLoadLikeCountResponse(likeCount);
    }
}

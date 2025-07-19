package cotato.timetile.domain.comment.api.response;

public record CommentLoadLikeResponse(
        int likeCount
) {
    public static CommentLoadLikeResponse of(int likeCount) {
        return new CommentLoadLikeResponse(likeCount);
    }
}

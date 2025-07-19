package cotato.timetile.domain.scrap.api.response;

public record ScrapCountResponse(
        int likeCount
) {
    public static ScrapCountResponse of(int likeCount) {
        return new ScrapCountResponse(likeCount);
    }
}

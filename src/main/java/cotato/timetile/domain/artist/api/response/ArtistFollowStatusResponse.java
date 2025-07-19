package cotato.timetile.domain.artist.api.response;

public record ArtistFollowStatusResponse(
        boolean isFollowing
) {
    public static ArtistFollowStatusResponse of(boolean isFollowing) {
        return new ArtistFollowStatusResponse(isFollowing);
    }
}

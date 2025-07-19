package cotato.timetile.domain.artist.api.response;

import cotato.timetile.domain.artist.domain.Artist;

public record ArtistLoadResponse(
        String id,
        String name,
        String imageUrl,
        int followCount
) {
    public static ArtistLoadResponse of(Artist artist) {
        return new ArtistLoadResponse(
                artist.getId(),
                artist.getName(),
                artist.getImageUrl(),
                artist.getFollowCount()
        );
    }
}

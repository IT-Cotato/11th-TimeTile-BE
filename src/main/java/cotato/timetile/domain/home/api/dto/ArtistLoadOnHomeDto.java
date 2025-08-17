package cotato.timetile.domain.home.api.dto;

import cotato.timetile.domain.artist.domain.Artist;

public record ArtistLoadOnHomeDto(
        String id,
        String name,
        String imageUrl
) {
    public static ArtistLoadOnHomeDto of(Artist artist) {
        return new ArtistLoadOnHomeDto(
                artist.getId(),
                artist.getName(),
                artist.getImageUrl()
        );
    }
}

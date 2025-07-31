package cotato.timetile.domain.profile.api.dto;

import cotato.timetile.domain.artist.domain.Artist;

public record ArtistFilterDto(
        String id,
        String name
) {
    public static ArtistFilterDto of(Artist artist) {
        return new ArtistFilterDto(
                artist.getId(),
                artist.getName()
        );
    }
}

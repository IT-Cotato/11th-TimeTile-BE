package cotato.timetile.domain.event.api.dto;

import cotato.timetile.domain.artist.domain.Artist;

public record RelatedArtistDto(
        String id,
        String name,
        String imageUrl
) {
    public static RelatedArtistDto of(Artist artist) {
        return new RelatedArtistDto(
                artist.getId(),
                artist.getName(),
                artist.getImageUrl()
        );
    }
}

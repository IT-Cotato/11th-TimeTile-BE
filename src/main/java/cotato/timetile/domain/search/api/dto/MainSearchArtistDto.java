package cotato.timetile.domain.search.api.dto;

import cotato.timetile.domain.artist.domain.ArtistDocument;

public record MainSearchArtistDto(
        String id,
        String name,
        String imageUrl
) {
    public static MainSearchArtistDto of(ArtistDocument artist) {
        return new MainSearchArtistDto(
                artist.getId(),
                artist.getName(),
                artist.getImageUrl()
        );
    }
}

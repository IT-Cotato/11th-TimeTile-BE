package cotato.timetile.domain.artist.api.dto;

import cotato.timetile.domain.artist.domain.ArtistDocument;

public record ArtistSearchDto(
        String id,
        String name,
        String imageUrl
) {
    public static ArtistSearchDto of(ArtistDocument artist) {
        return new ArtistSearchDto(
                artist.getId(),
                artist.getName(),
                artist.getImageUrl()
        );
    }
}

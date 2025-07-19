package cotato.timetile.domain.artist.api.response;

import cotato.timetile.domain.artist.api.dto.ArtistSearchDto;
import java.util.List;

public record ArtistSearchResponse(
        List<ArtistSearchDto> artists
) {
    public static ArtistSearchResponse of(List<ArtistSearchDto> artists) {
        return new ArtistSearchResponse(artists);
    }
}

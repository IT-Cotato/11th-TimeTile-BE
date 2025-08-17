package cotato.timetile.domain.home.api.response;

import cotato.timetile.domain.home.api.dto.ArtistLoadOnHomeDto;
import java.util.List;

public record ArtistLoadOnHomeResponse(
        List<ArtistLoadOnHomeDto> artists
) {
    public static ArtistLoadOnHomeResponse of(List<ArtistLoadOnHomeDto> artists) {
        return new ArtistLoadOnHomeResponse(artists);
    }
}

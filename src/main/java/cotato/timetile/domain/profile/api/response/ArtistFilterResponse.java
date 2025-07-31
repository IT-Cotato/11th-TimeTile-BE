package cotato.timetile.domain.profile.api.response;

import cotato.timetile.domain.profile.api.dto.ArtistFilterDto;
import java.util.List;

public record ArtistFilterResponse(
        List<ArtistFilterDto> artists
) {
    public static ArtistFilterResponse of(List<ArtistFilterDto> artists) {
        return new ArtistFilterResponse(artists);
    }
}

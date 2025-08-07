package cotato.timetile.domain.search.api.response;

import cotato.timetile.domain.artist.domain.ArtistDocument;
import cotato.timetile.domain.search.api.dto.MainSearchArtistDto;
import java.util.List;
import org.springframework.data.domain.Page;

public record MainSearchArtistResponse(
        List<MainSearchArtistDto> artists,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static MainSearchArtistResponse of(List<MainSearchArtistDto> artists, Page<ArtistDocument> artistPage) {
        return new MainSearchArtistResponse(
                artists,
                artistPage.getNumber() + 1,
                artistPage.getSize(),
                artistPage.getTotalPages(),
                artistPage.getTotalElements(),
                artistPage.hasNext(),
                artistPage.hasPrevious(),
                artistPage.isLast()
        );
    }
}

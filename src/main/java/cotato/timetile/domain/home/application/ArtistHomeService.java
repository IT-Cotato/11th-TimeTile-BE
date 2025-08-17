package cotato.timetile.domain.home.application;

import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.artist.persistence.ArtistRepository;
import cotato.timetile.domain.home.api.dto.ArtistLoadOnHomeDto;
import cotato.timetile.domain.home.api.response.ArtistLoadOnHomeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistHomeService {

    private static final int SIZE = 6;
    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public ArtistLoadOnHomeResponse load() {
        List<Artist> artists = artistRepository.findAllByOrderByPostCountDescLimitN(SIZE);
        return ArtistLoadOnHomeResponse.of(
                artists.stream()
                        .map(ArtistLoadOnHomeDto::of)
                        .toList()
        );
    }

}

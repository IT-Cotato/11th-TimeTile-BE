package cotato.timetile.domain.artist.application;

import cotato.timetile.domain.artist.api.response.ArtistLoadResponse;
import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.artist.persistence.ArtistRepository;
import cotato.timetile.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistLoadService {

    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public ArtistLoadResponse load(String artistId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(NotFoundException::wrong);
        return ArtistLoadResponse.of(artist);
    }
    
}

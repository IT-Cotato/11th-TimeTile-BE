package cotato.timetile.domain.artist.application;

import cotato.timetile.domain.artist.api.dto.SpotifyArtistInfoDto;
import cotato.timetile.domain.artist.api.request.ArtistUpdateRequest;
import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.artist.domain.ArtistDocument;
import cotato.timetile.domain.artist.persistence.ArtistDocumentRepository;
import cotato.timetile.domain.artist.persistence.ArtistRepository;
import cotato.timetile.global.handler.SpotifyHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistUpdateService {

    private final ArtistRepository artistRepository;
    private final ArtistDocumentRepository artistDocumentRepository;
    private final SpotifyHandler spotifyHandler;

    @Transactional
    public void updateArtist(ArtistUpdateRequest request) {
        List<SpotifyArtistInfoDto> spotifyArtists = spotifyHandler.getArtistInfo(request.spotifyIds());
        spotifyArtists.forEach(
                spotifyArtist -> {
                    String id = spotifyArtist.id();
                    String name = spotifyArtist.name();
                    String imageUrl = spotifyArtist.imageUrl();
                    Artist artist = artistRepository.findById(id)
                            .orElseGet(() -> {
                                Artist newArtist = Artist.builder()
                                        .id(id)
                                        .name(name)
                                        .imageUrl(imageUrl)
                                        .build();
                                artistRepository.save(newArtist);
                                return newArtist;
                            });
                    artist.update(spotifyArtist.name(), spotifyArtist.imageUrl());
                    artistDocumentRepository.save(ArtistDocument.of(artist));
                }
        );
    }

}

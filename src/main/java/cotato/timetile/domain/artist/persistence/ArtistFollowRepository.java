package cotato.timetile.domain.artist.persistence;

import cotato.timetile.domain.artist.domain.ArtistFollow;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistFollowRepository extends JpaRepository<ArtistFollow, Long> {
    Optional<ArtistFollow> findByArtistIdAndUserId(String artistId, Long userId);
}

package cotato.timetile.domain.artist.persistence;

import cotato.timetile.domain.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {
}

package cotato.timetile.domain.artist.persistence;

import cotato.timetile.domain.artist.domain.Artist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {

    @Query(value = """
                SELECT a
                FROM Artist a
                LEFT JOIN a.posts p
                GROUP BY a
                ORDER BY count(p) DESC
                LIMIT :n
            """
    )
    List<Artist> findAllByOrderByPostCountDescLimitN(@Param("n") int n);
}

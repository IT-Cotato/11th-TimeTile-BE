package cotato.timetile.domain.post.persistence;

import cotato.timetile.domain.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
            SELECT * FROM (
                SELECT p.*, ROW_NUMBER() OVER (
                           PARTITION BY MONTH(p.started_at)
                           ORDER BY p.like_count DESC
                       ) AS rn
                FROM posts p
                JOIN users u ON p.author_id = u.id
                WHERE p.artist_id = :artistId
                  AND YEAR(p.started_at) = :year
                  AND p.visibility = 'PUBLIC'
                  AND u.visibility = 'PUBLIC'
            ) ranked
            WHERE rn <= 2
            ORDER BY MONTH(started_at), like_count DESC;
            """, nativeQuery = true)
    List<Post> findTop2PostsPerMonthByArtistAndYear(@Param("artistId") String artistId,
                                                    @Param("year") int year);

    Page<Post> findAllByGroupId(String groupId, Pageable pageable);

}

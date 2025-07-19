package cotato.timetile.domain.event.persistence;

import cotato.timetile.domain.event.domain.ChangeType;
import cotato.timetile.domain.event.domain.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = """
            SELECT e1 FROM Event e1
            WHERE e1.artist.id = :artistId
            AND FUNCTION('YEAR', e1.startedAt) = :year
            AND e1.active = TRUE
            AND e1.editedAt = (SELECT MAX(e2.editedAt) FROM Event e2
                              WHERE e2.groupId = e1.groupId
                              AND e2.artist.id = :artistId
                              AND e2.active = TRUE)
            """
    )
    List<Event> findAllByArtistIdAndActiveIsTrueAndYear(@Param("artistId") String artistId,
                                                        @Param("year") int year);

    @Query(value = """ 
            SELECT e FROM Event e
            WHERE (e.active = FALSE)
            AND (:changeType IS NULL OR e.changeType = :changeType)
            """)
    Page<Event> findAllByActiveIsFalseOrderByEditedAtDesc(@Param("changeType") ChangeType changeType,
                                                          Pageable pageable);

    Optional<Event> findTopByGroupIdAndActiveIsTrueOrderByIdDesc(String groupId);

    List<Event> findAllByGroupIdAndActiveIsTrueOrderByEditedAtDesc(String groupId);

    boolean existsByGroupIdAndAuthorIdAndActiveIsTrue(String groupId, Long authorId);

}

package cotato.timetile.domain.term.persistence;

import cotato.timetile.domain.term.domain.Term;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    @Query(value = """
            SELECT *
            FROM (
                SELECT *, ROW_NUMBER() OVER (PARTITION BY title ORDER BY version DESC) as rn
                FROM terms
            ) ranked
            WHERE rn = 1
            ORDER BY id
            """, nativeQuery = true)
    List<Term> findLatestTerms();

}

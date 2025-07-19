package cotato.timetile.domain.scrap.persistence;

import cotato.timetile.domain.scrap.domain.Scrap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findByPostIdAndScrapFolderId(Long postId, Long scrapFolderId);

}

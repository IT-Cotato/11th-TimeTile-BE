package cotato.timetile.domain.scrap.persistence;

import cotato.timetile.domain.scrap.domain.ScrapFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapFolderRepository extends JpaRepository<ScrapFolder, Long> {
}

package cotato.timetile.domain.user.persistence;

import cotato.timetile.domain.user.domain.UserVisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVisitLogRepository extends JpaRepository<UserVisitLog, Long> {
}

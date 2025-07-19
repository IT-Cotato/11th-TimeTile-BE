package cotato.timetile.domain.user.persistence;

import cotato.timetile.domain.user.domain.UserFollow;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    Optional<UserFollow> findByFollowingIdAndFollowerId(Long followingId, Long followerId);

}

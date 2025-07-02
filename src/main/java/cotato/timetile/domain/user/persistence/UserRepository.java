package cotato.timetile.domain.user.persistence;

import cotato.timetile.domain.user.domain.AuthProvider;
import cotato.timetile.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
    
}

package cotato.timetile.domain.comment.persistence;

import cotato.timetile.domain.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostIdOrderByTimeInfoCreatedAtAsc(Long postId);

}

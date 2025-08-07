package cotato.timetile.domain.user.persistence;

import cotato.timetile.domain.user.domain.UserDocument;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocumentRepository extends ElasticsearchRepository<UserDocument, String> {

    List<UserDocument> findAllByNicknameMatches(String query);

    Page<UserDocument> findAllByNicknameMatches(String query, Pageable pageable);

}

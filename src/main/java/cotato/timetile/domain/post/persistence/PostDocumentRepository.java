package cotato.timetile.domain.post.persistence;

import cotato.timetile.domain.post.domain.PostDocument;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, String> {

    List<PostDocument> findAllByTitleMatchesOrContentMatches(String titleQuery, String contentQuery);

    Page<PostDocument> findAllByTitleMatchesOrContentMatches(String titleQuery, String contentQuery, Pageable pageable);

    List<PostDocument> findAllByTitleMatchesOrContentMatchesAndIdLessThanOrderByIdDesc(String titleQuery,
                                                                                       String contentQuery,
                                                                                       String lastPostId);

}

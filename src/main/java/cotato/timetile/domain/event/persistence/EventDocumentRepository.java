package cotato.timetile.domain.event.persistence;

import cotato.timetile.domain.event.domain.EventDocument;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDocumentRepository extends ElasticsearchRepository<EventDocument, String> {

    List<EventDocument> findAllByNameMatchesOrArtistNameMatches(String nameQuery, String artistNameQuery);

    Page<EventDocument> findAllByNameMatchesOrArtistNameMatches(String nameQuery, String artistNameQuery,
                                                                Pageable pageable);

    void deleteAllByGroupId(String groupId);

}

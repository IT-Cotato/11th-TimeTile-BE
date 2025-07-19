package cotato.timetile.domain.event.persistence;

import cotato.timetile.domain.event.domain.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDocumentRepository extends ElasticsearchRepository<EventDocument, String> {
}

package cotato.timetile.domain.search.persistence;

import cotato.timetile.domain.search.domain.SuggestionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchSuggestionRepository extends ElasticsearchRepository<SuggestionDocument, String> {
}

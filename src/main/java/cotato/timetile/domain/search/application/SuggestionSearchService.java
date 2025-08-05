package cotato.timetile.domain.search.application;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import cotato.timetile.domain.search.api.response.SuggestionSearchLoadResponse;
import cotato.timetile.domain.search.domain.SuggestionDocument;
import cotato.timetile.domain.search.persistence.SearchSuggestionRepository;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuggestionSearchService {

    private static final int MAX_SIZE = 10;
    private final SearchSuggestionRepository searchSuggestionRepository;
    private final ElasticsearchClient elasticsearchClient;

    public void add(String query) {
        SuggestionDocument suggestionDocument = searchSuggestionRepository.findById(query)
                .orElseGet(() -> SuggestionDocument.of(query));
        searchSuggestionRepository.save(suggestionDocument);
    }

    public SuggestionSearchLoadResponse load(String prefix) {
        try {
            SearchResponse<SuggestionDocument> response = elasticsearchClient.search(
                    s -> s.index("suggestions")
                            .size(MAX_SIZE)
                            .query(q -> q.bool(b -> b
                                    .must(m1 -> m1.range(r -> r.number(v -> v
                                            .field("length")
                                            .gte((double) prefix.length()))
                                    )).should(s1 -> s1.match(m -> m
                                            .field("query.keyword")
                                            .query(prefix)
                                            .boost(10.0f)
                                    ))
                                    .should(s2 -> s2.matchPhrasePrefix(m -> m
                                            .field("query.edge")
                                            .query(prefix)
                                            .boost(5.0f)
                                    ))
                                    .should(s3 -> s3.match(m -> m
                                            .field("query")
                                            .query(prefix)
                                            .boost(3.0f)
                                    ))
                            ))
                            .sort(sort -> sort
                                    .score(o -> o.order(SortOrder.Desc))
                            )
                            .trackTotalHits(t -> t.enabled(false)), SuggestionDocument.class);
            return SuggestionSearchLoadResponse.of(
                    response.hits().hits().stream()
                            .map(Hit::source)
                            .filter(Objects::nonNull)
                            .toList().stream()
                            .map(SuggestionDocument::getQuery)
                            .toList()
            );
        } catch (IOException e) {
            throw cotato.timetile.global.exception.IOException.external();
        }
    }

}

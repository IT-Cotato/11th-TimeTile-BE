package cotato.timetile.domain.search.application;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import cotato.timetile.domain.search.api.response.SuggestionSearchLoadResponse;
import cotato.timetile.domain.search.domain.SuggestionDocument;
import cotato.timetile.domain.search.persistence.SearchSuggestionRepository;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuggestionSearchService {

    private static final int MAX_SIZE = 10;
    private final SearchSuggestionRepository searchSuggestionRepository;
    private final ElasticsearchClient elasticsearchClient;

    public void add(String query) {
        Optional<SuggestionDocument> optionalSuggestion = searchSuggestionRepository.findById(query);
        SuggestionDocument suggestionDocument;
        if (optionalSuggestion.isPresent()) {
            suggestionDocument = optionalSuggestion.get();
            suggestionDocument.increaseCount();
        } else {
            suggestionDocument = SuggestionDocument.of(query);
        }
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
                                    ))
                                    .should(s1 -> s1.prefix(m -> m
                                            .field("query.keyword")
                                            .value(prefix)
                                            .boost(5.0f)
                                    ))
                                    .should(s2 -> s2.matchPhrasePrefix(m -> m
                                            .field("query.edge")
                                            .query(prefix)
                                            .boost(3.0f)
                                    ))
                                    .should(s3 -> s3.match(m -> m
                                            .field("query")
                                            .query(prefix)
                                            .boost(2.0f)
                                    ))
                                    .should(s4 -> s4.fuzzy(f -> f
                                            .field("query")
                                            .value(prefix)
                                            .fuzziness("AUTO")
                                            .boost(0.8f)
                                    ))
                                    .should(s5 -> s5.functionScore(fs -> fs
                                            .functions(f -> f.fieldValueFactor(fvf -> fvf
                                                    .field("count")
                                                    .factor(1.2)
                                                    .modifier(FieldValueFactorModifier.Log1p)
                                                    .missing(1.0)
                                            ))
                                            .boostMode(FunctionBoostMode.Multiply)
                                    ))
                            ))
                            .minScore(2.5)
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

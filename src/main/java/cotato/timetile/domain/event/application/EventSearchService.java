package cotato.timetile.domain.event.application;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import cotato.timetile.domain.event.api.dto.EventSearchDto;
import cotato.timetile.domain.event.api.response.EventSearchResponse;
import cotato.timetile.domain.event.domain.EventDocument;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public EventSearchResponse search(String query) {
        String[] keywords = query.split("\\s+");
        try {
            SearchResponse<EventDocument> response = elasticsearchClient.search(s -> s
                    .index("events")
                    .size(5)
                    .query(q -> q.bool(b -> {
                        for (String keyword : keywords) {
                            b.should(s1 -> s1.match(m -> m.field("name.keyword").query(keyword).boost(10.0f)));
                            b.should(s2 -> s2.matchPhrasePrefix(m -> m.field("name.edge").query(keyword)
                                    .boost(7.0f)));
                            b.should(s3 -> s3.match(m -> m.field("name").query(keyword).boost(5.0f)));
                            b.should(s4 -> s4.match(
                                    m -> m.field("artistName.keyword").query(keyword).boost(10.0f)));
                            b.should(s5 -> s5.matchPhrasePrefix(
                                    m -> m.field("artistName.edge").query(keyword).boost(7.0f)));
                            b.should(s6 -> s6.match(m -> m.field("artistName").query(keyword).boost(5.0f)));
                        }
                        return b;
                    }))
                    .trackTotalHits(t -> t.enabled(false)), EventDocument.class);
            return EventSearchResponse.of(
                    response.hits().hits().stream()
                            .map(Hit::source)
                            .filter(Objects::nonNull)
                            .map(EventSearchDto::of)
                            .toList()
            );
        } catch (IOException e) {
            throw cotato.timetile.global.exception.IOException.external();
        }
    }

}

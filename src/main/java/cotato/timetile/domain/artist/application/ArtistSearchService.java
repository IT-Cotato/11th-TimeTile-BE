package cotato.timetile.domain.artist.application;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import cotato.timetile.domain.artist.api.dto.ArtistSearchDto;
import cotato.timetile.domain.artist.api.response.ArtistSearchResponse;
import cotato.timetile.domain.artist.domain.ArtistDocument;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public ArtistSearchResponse search(String query) {
        try {
            SearchResponse<ArtistDocument> response = elasticsearchClient.search(s -> s.index("artists")
                    .size(5)
                    .query(q -> q.bool(b -> b
                            .should(s1 -> s1.match(m -> m
                                    .field("name.keyword")
                                    .query(query)
                                    .boost(10.0f)
                            ))
                            .should(s2 -> s2.matchPhrasePrefix(m -> m
                                    .field("name.edge")
                                    .query(query)
                                    .boost(5.0f)
                            ))
                            .should(s3 -> s3.match(m -> m
                                    .field("name")
                                    .query(query)
                                    .boost(3.0f)
                            ))
                    ))
                    .trackTotalHits(t -> t.enabled(false)), ArtistDocument.class);
            return ArtistSearchResponse.of(
                    response.hits().hits().stream()
                            .map(Hit::source)
                            .filter(Objects::nonNull)
                            .toList().stream()
                            .map(ArtistSearchDto::of)
                            .toList()
            );
        } catch (IOException e) {
            throw cotato.timetile.global.exception.IOException.external();
        }

    }
}

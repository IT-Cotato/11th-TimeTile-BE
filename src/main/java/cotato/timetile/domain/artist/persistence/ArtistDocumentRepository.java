package cotato.timetile.domain.artist.persistence;

import cotato.timetile.domain.artist.domain.ArtistDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistDocumentRepository extends ElasticsearchRepository<ArtistDocument, String> {
}

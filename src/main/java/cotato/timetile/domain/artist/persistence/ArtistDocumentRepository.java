package cotato.timetile.domain.artist.persistence;

import cotato.timetile.domain.artist.domain.ArtistDocument;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistDocumentRepository extends ElasticsearchRepository<ArtistDocument, String> {

    List<ArtistDocument> findAllByNameMatches(String query);

    Page<ArtistDocument> findAllByNameMatches(String query, Pageable pageable);

}

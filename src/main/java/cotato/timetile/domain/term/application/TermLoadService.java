package cotato.timetile.domain.term.application;

import cotato.timetile.domain.term.api.dto.TermLoadDto;
import cotato.timetile.domain.term.api.response.TermLoadResponse;
import cotato.timetile.domain.term.persistence.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermLoadService {

    private final TermRepository termRepository;

    public TermLoadResponse load() {
        return TermLoadResponse.of(
                termRepository.findLatestTerms().stream()
                        .map(TermLoadDto::of)
                        .toList()
        );
    }

}

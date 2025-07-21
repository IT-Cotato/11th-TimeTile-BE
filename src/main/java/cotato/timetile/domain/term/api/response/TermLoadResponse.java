package cotato.timetile.domain.term.api.response;

import cotato.timetile.domain.term.api.dto.TermLoadDto;
import java.util.List;

public record TermLoadResponse(
        List<TermLoadDto> terms
) {
    public static TermLoadResponse of(List<TermLoadDto> terms) {
        return new TermLoadResponse(terms);
    }
}

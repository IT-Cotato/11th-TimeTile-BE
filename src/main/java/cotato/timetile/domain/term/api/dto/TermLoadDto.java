package cotato.timetile.domain.term.api.dto;

import cotato.timetile.domain.term.domain.Term;

public record TermLoadDto(
        Long id,
        String title,
        String content,
        boolean required
) {
    public static TermLoadDto of(Term term) {
        return new TermLoadDto(
                term.getId(),
                term.getTitle(),
                term.getContent(),
                term.isRequired()
        );
    }
}

package cotato.timetile.domain.search.api.response;

import java.util.List;

public record SuggestionSearchLoadResponse(
        List<String> suggestions
) {
    public static SuggestionSearchLoadResponse of(List<String> suggestions) {
        return new SuggestionSearchLoadResponse(suggestions);
    }
}

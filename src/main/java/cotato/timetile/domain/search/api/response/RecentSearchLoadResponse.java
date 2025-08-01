package cotato.timetile.domain.search.api.response;

import java.util.List;

public record RecentSearchLoadResponse(
        List<String> results
) {
    public static RecentSearchLoadResponse of(List<String> results) {
        return new RecentSearchLoadResponse(results);
    }
}

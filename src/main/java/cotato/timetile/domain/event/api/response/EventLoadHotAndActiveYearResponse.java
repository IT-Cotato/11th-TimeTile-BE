package cotato.timetile.domain.event.api.response;

import java.util.List;
import java.util.Map;

public record EventLoadHotAndActiveYearResponse(
        Map<Integer, List<String>> activeYears
) {
    public static EventLoadHotAndActiveYearResponse of(Map<Integer, List<String>> activeYears) {
        return new EventLoadHotAndActiveYearResponse(activeYears);
    }
}

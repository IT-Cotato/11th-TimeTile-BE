package cotato.timetile.domain.event.api.response;

import cotato.timetile.domain.event.api.dto.EventSearchDto;
import java.util.List;

public record EventSearchResponse(
        List<EventSearchDto> events
) {
    public static EventSearchResponse of(List<EventSearchDto> events) {
        return new EventSearchResponse(events);
    }
}

package cotato.timetile.domain.event.api.response;

import cotato.timetile.domain.event.api.dto.EventLoadHotDto;
import java.util.List;
import java.util.Map;

public record EventLoadHotResponse(
        Map<Integer, List<EventLoadHotDto>> events
) {
    public static EventLoadHotResponse of(Map<Integer, List<EventLoadHotDto>> events) {
        return new EventLoadHotResponse(events);
    }
}

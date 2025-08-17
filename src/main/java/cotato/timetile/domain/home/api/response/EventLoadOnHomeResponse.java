package cotato.timetile.domain.home.api.response;

import cotato.timetile.domain.home.api.dto.EventLoadOnHomeDto;
import java.util.List;

public record EventLoadOnHomeResponse(
        List<EventLoadOnHomeDto> events
) {
    public static EventLoadOnHomeResponse of(List<EventLoadOnHomeDto> events) {
        return new EventLoadOnHomeResponse(events);
    }
}

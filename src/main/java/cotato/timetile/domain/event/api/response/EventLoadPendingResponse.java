package cotato.timetile.domain.event.api.response;

import cotato.timetile.domain.event.api.dto.EventLoadPendingDto;
import java.util.List;

public record EventLoadPendingResponse(
        List<EventLoadPendingDto> pendingEvents
) {
    public static EventLoadPendingResponse of(List<EventLoadPendingDto> pendingEvents) {
        return new EventLoadPendingResponse(pendingEvents);
    }
}

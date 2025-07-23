package cotato.timetile.domain.event.api.response;

import cotato.timetile.domain.event.api.dto.EventLoadMoreDto;
import java.util.List;

public record EventLoadMoreResponse(
        List<EventLoadMoreDto> events,
        boolean hasNext,
        Integer lastDay,
        Long lastEventId
) {
    public static EventLoadMoreResponse of(List<EventLoadMoreDto> events, boolean hasNext, Integer lastDay,
                                           Long lastEventId) {
        return new EventLoadMoreResponse(events, hasNext, lastDay, lastEventId);
    }
}

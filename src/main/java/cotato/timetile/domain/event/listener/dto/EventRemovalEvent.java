package cotato.timetile.domain.event.listener.dto;

import cotato.timetile.domain.event.domain.Event;

public record EventRemovalEvent(
        String id
) {
    public static EventRemovalEvent of(Event event) {
        return new EventRemovalEvent(event.getId().toString());
    }
}

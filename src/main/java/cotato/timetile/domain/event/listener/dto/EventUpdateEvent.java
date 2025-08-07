package cotato.timetile.domain.event.listener.dto;

import cotato.timetile.domain.event.domain.Event;

public record EventUpdateEvent(
        Long id
) {
    public static EventUpdateEvent of(Event event) {
        return new EventUpdateEvent(event.getId());
    }
}

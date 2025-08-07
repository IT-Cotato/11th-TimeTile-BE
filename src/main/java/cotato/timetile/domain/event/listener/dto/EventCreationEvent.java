package cotato.timetile.domain.event.listener.dto;

import cotato.timetile.domain.event.domain.Event;

public record EventCreationEvent(
        Long id
) {
    public static EventCreationEvent of(Event event) {
        return new EventCreationEvent(event.getId());
    }
}

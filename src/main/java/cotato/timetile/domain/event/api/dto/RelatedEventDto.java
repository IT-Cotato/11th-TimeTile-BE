package cotato.timetile.domain.event.api.dto;

import cotato.timetile.domain.event.domain.Event;

public record RelatedEventDto(
        String groupId,
        String name
) {
    public static RelatedEventDto of(Event event) {
        return new RelatedEventDto(
                event.getGroupId(),
                event.getName()
        );
    }
}

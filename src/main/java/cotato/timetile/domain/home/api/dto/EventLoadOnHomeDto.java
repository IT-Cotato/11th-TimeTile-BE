package cotato.timetile.domain.home.api.dto;

import cotato.timetile.domain.event.domain.Event;
import java.time.LocalDateTime;

public record EventLoadOnHomeDto(
        String artistName,
        String groupId,
        String name,
        LocalDateTime editedAt
) {
    public static EventLoadOnHomeDto of(Event event) {
        return new EventLoadOnHomeDto(
                event.getArtist().getName(),
                event.getGroupId(),
                event.getName(),
                event.getEditedAt()
        );
    }
}

package cotato.timetile.domain.event.api.dto;

import cotato.timetile.domain.event.domain.EventDocument;
import java.time.LocalDate;

public record EventSearchDto(
        Long id,
        String groupId,
        String name,
        String artistName,
        LocalDate startedAt
) {
    public static EventSearchDto of(EventDocument event) {
        return new EventSearchDto(
                Long.parseLong(event.getId()),
                event.getGroupId(),
                event.getName(),
                event.getArtistName(),
                event.getStartedAt()
        );
    }
}

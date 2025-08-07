package cotato.timetile.domain.search.api.dto;

import cotato.timetile.domain.event.domain.ActivityType;
import cotato.timetile.domain.event.domain.Event;
import java.time.LocalDate;
import java.util.List;

public record MainSearchEventDto(
        String groupId,
        String name,
        String description,
        List<ActivityType> activityTypes,
        LocalDate startedAt,
        String artistName,
        String artistImageUrl
) {
    public static MainSearchEventDto of(Event event) {
        return new MainSearchEventDto(
                event.getGroupId(),
                event.getName(),
                event.getDescription(),
                event.getActivityTypes(),
                event.getStartedAt(),
                event.getArtist().getName(),
                event.getArtist().getImageUrl()
        );
    }
}

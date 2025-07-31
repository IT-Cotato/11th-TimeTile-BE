package cotato.timetile.domain.profile.api.dto;

import cotato.timetile.domain.event.domain.Event;
import java.time.LocalDate;
import java.util.List;

public record EventLoadAllOnProfileDto(
        String groupId,
        String name,
        String description,
        LocalDate startedAt,
        List<String> relatedMaterials,
        int contributorCount
) {
    public static EventLoadAllOnProfileDto of(Event event) {
        return new EventLoadAllOnProfileDto(
                event.getGroupId(),
                event.getName(),
                event.getDescription(),
                event.getStartedAt(),
                event.getRelatedMaterials(),
                event.getContributorCount()
        );
    }
}

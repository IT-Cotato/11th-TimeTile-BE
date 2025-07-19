package cotato.timetile.domain.event.api.dto;

import cotato.timetile.domain.event.api.request.EventCreationRequest;
import cotato.timetile.domain.event.api.request.EventUpdateRequest;
import cotato.timetile.domain.event.domain.ActivityType;
import cotato.timetile.domain.event.domain.ChangeType;
import cotato.timetile.domain.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record EventCreationDto(
        String groupId,
        String name,
        String description,
        String source,
        List<ActivityType> activityTypes,
        List<String> relatedEvents,
        List<String> relatedMaterials,
        LocalDate startedAt,
        LocalDate endedAt,
        ChangeType changeType,
        int contributorCount,
        int postCount,
        User author
) {
    public static EventCreationDto of(EventCreationRequest request, User author) {
        return new EventCreationDto(
                UUID.randomUUID().toString(),
                request.name(),
                request.description(),
                request.source(),
                request.activityTypes(),
                request.relatedEvents(),
                request.relatedMaterials(),
                request.startedAt(),
                request.endedAt(),
                ChangeType.NEW,
                1,
                0,
                author
        );
    }

    public static EventCreationDto of(EventUpdateRequest request, String groupId, int contributorCount, int postCount,
                                      User author) {
        return new EventCreationDto(
                groupId,
                request.name(),
                request.description(),
                request.source(),
                request.activityTypes(),
                request.relatedEvents(),
                request.relatedMaterials(),
                request.startedAt(),
                request.endedAt(),
                ChangeType.EDITED,
                contributorCount,
                postCount,
                author
        );
    }
}

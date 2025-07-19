package cotato.timetile.domain.event.api.dto;

import cotato.timetile.domain.event.domain.ActivityType;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.RelatedArtist;
import java.time.LocalDate;
import java.util.List;

public record EventLoadHotDto(
        Long eventId,
        String groupId,
        String name,
        String description,
        String source,
        List<RelatedEventDto> relatedEvents,
        List<RelatedArtistDto> relatedArtists,
        List<ActivityType> activityTypes,
        List<String> relatedMaterials,
        LocalDate startedAt,
        LocalDate endedAt,
        int contributorCount
) {
    public static EventLoadHotDto of(Event event, List<RelatedEventDto> relatedEvents) {
        return new EventLoadHotDto(
                event.getId(),
                event.getGroupId(),
                event.getName(),
                event.getDescription(),
                event.getSource(),
                relatedEvents,
                event.getRelatedArtists().stream().map(RelatedArtist::getArtist).map(RelatedArtistDto::of).toList(),
                event.getActivityTypes(),
                event.getRelatedMaterials(),
                event.getStartedAt(),
                event.getEndedAt(),
                event.getContributorCount()
        );
    }
}

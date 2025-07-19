package cotato.timetile.domain.event.api.response;

import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.event.api.dto.RelatedEventDto;
import cotato.timetile.domain.event.domain.ActivityType;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.RelatedArtist;
import java.time.LocalDate;
import java.util.List;

public record EventLoadResponse(
        String name,
        String description,
        String source,
        List<ActivityType> activityTypes,
        List<RelatedEventDto> relatedEvents,
        List<String> relatedArtists,
        List<String> relatedMaterials,
        LocalDate startedAt,
        LocalDate endedAt
) {
    public static EventLoadResponse of(Event event, List<RelatedEventDto> relatedEvents) {
        return new EventLoadResponse(
                event.getName(),
                event.getDescription(),
                event.getSource(),
                event.getActivityTypes(),
                relatedEvents,
                event.getRelatedArtists().stream().map(RelatedArtist::getArtist).map(Artist::getName).toList(),
                event.getRelatedMaterials(),
                event.getStartedAt(),
                event.getEndedAt()
        );
    }
}

package cotato.timetile.domain.event.api.dto;

import cotato.timetile.domain.event.domain.ActivityType;
import cotato.timetile.domain.event.domain.ChangeType;
import cotato.timetile.global.util.DiffMatchPatcher.Diff;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public record EventLoadPendingDto(
        Long eventId,
        LinkedList<Diff> name,
        LinkedList<Diff> description,
        LinkedList<Diff> source,
        LinkedList<Diff> startedAt,
        LinkedList<Diff> endedAt,
        List<RelatedArtistDto> addedRelatedArtists,
        List<RelatedArtistDto> missingRelatedArtists,
        List<RelatedEventDto> addedRelatedEvents,
        List<RelatedEventDto> missingRelatedEvents,
        List<ActivityType> addedActivityTypes,
        List<ActivityType> missingActivityTypes,
        List<String> addedRelatedMaterials,
        List<String> missingRelatedMaterials,
        LocalDateTime editedAt,
        ChangeType changeType
) {
    public static EventLoadPendingDto of(Long eventId,
                                         LinkedList<Diff> name,
                                         LinkedList<Diff> description,
                                         LinkedList<Diff> source,
                                         LinkedList<Diff> startedAt,
                                         LinkedList<Diff> endedAt,
                                         List<RelatedArtistDto> addedRelatedArtists,
                                         List<RelatedArtistDto> missingRelatedArtists,
                                         List<RelatedEventDto> addedRelatedEvents,
                                         List<RelatedEventDto> missingRelatedEvents,
                                         List<ActivityType> addedActivityTypes,
                                         List<ActivityType> missingActivityTypes,
                                         List<String> addedRelatedMaterials,
                                         List<String> missingRelatedMaterials,
                                         LocalDateTime editedAt,
                                         ChangeType changeType) {
        return new EventLoadPendingDto(
                eventId,
                name,
                description,
                source,
                startedAt,
                endedAt,
                addedRelatedArtists,
                missingRelatedArtists,
                addedRelatedEvents,
                missingRelatedEvents,
                addedActivityTypes,
                missingActivityTypes,
                addedRelatedMaterials,
                missingRelatedMaterials,
                editedAt,
                changeType
        );
    }
}

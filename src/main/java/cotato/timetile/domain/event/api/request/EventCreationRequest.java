package cotato.timetile.domain.event.api.request;

import cotato.timetile.domain.event.domain.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record EventCreationRequest(
        @Size(max = 50, min = 1) String name,
        @Size(max = 200, min = 1) String description,
        @NotBlank String source,
        @Size(min = 1) List<ActivityType> activityTypes,
        List<String> relatedEvents,
        List<String> relatedArtists,
        List<String> relatedMaterials,
        @NotNull LocalDate startedAt,
        @NotNull LocalDate endedAt,
        @NotBlank String artistId
) {
}

package cotato.timetile.domain.profile.api.response;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.profile.api.dto.EventLoadAllOnProfileDto;
import java.util.List;
import org.springframework.data.domain.PageImpl;

public record EventLoadAllOnPageResponse(
        List<EventLoadAllOnProfileDto> events,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static EventLoadAllOnPageResponse of(List<EventLoadAllOnProfileDto> events, PageImpl<Event> eventPage) {
        return new EventLoadAllOnPageResponse(
                events,
                eventPage.getNumber() + 1,
                eventPage.getSize(),
                eventPage.getTotalPages(),
                eventPage.getTotalElements(),
                eventPage.hasNext(),
                eventPage.hasPrevious(),
                eventPage.isLast()
        );
    }
}

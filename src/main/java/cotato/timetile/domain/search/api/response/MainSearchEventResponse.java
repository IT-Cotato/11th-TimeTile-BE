package cotato.timetile.domain.search.api.response;

import cotato.timetile.domain.event.domain.EventDocument;
import cotato.timetile.domain.search.api.dto.MainSearchEventDto;
import java.util.List;
import org.springframework.data.domain.Page;

public record MainSearchEventResponse(
        List<MainSearchEventDto> events,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static MainSearchEventResponse of(List<MainSearchEventDto> events, Page<EventDocument> eventPage) {
        return new MainSearchEventResponse(
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

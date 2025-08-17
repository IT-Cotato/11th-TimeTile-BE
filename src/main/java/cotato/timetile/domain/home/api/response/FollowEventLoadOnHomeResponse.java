package cotato.timetile.domain.home.api.response;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.home.api.dto.FollowEventLoadOnHomeDto;
import java.util.List;
import org.springframework.data.domain.PageImpl;

public record FollowEventLoadOnHomeResponse(
        List<FollowEventLoadOnHomeDto> events,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static FollowEventLoadOnHomeResponse of(List<FollowEventLoadOnHomeDto> events, PageImpl<Event> eventPage) {
        return new FollowEventLoadOnHomeResponse(
                events,
                eventPage.hasNext(),
                eventPage.hasPrevious(),
                eventPage.isLast()
        );
    }
}

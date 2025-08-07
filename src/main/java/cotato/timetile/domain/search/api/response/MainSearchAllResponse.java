package cotato.timetile.domain.search.api.response;

import cotato.timetile.domain.search.api.dto.MainSearchArtistDto;
import cotato.timetile.domain.search.api.dto.MainSearchEventDto;
import cotato.timetile.domain.search.api.dto.MainSearchPostDto;
import cotato.timetile.domain.search.api.dto.MainSearchUserDto;
import java.util.List;

public record MainSearchAllResponse(
        int artistCount,
        List<MainSearchArtistDto> artists,
        int userCount,
        List<MainSearchUserDto> users,
        int postCount,
        List<MainSearchPostDto> posts,
        int eventCount,
        List<MainSearchEventDto> events
) {
    public static MainSearchAllResponse of(int artistCount, List<MainSearchArtistDto> artists,
                                           int userCount, List<MainSearchUserDto> users,
                                           int postCount, List<MainSearchPostDto> posts,
                                           int eventCount, List<MainSearchEventDto> events) {
        return new MainSearchAllResponse(
                artistCount, artists,
                userCount, users,
                postCount, posts,
                eventCount, events
        );
    }
}

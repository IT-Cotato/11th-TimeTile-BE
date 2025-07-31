package cotato.timetile.domain.profile.api.response;

import cotato.timetile.domain.profile.api.dto.FollowProfileDto;
import java.util.List;

public record FollowProfileResponse(
        List<FollowProfileDto> followers,
        boolean hasNext,
        Long lastFollowId
) {
    public static FollowProfileResponse of(List<FollowProfileDto> followers, boolean hasNext, Long lastFollowId) {
        return new FollowProfileResponse(followers, hasNext, lastFollowId);
    }
}

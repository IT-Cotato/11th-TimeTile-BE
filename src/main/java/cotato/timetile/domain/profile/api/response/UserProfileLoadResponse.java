package cotato.timetile.domain.profile.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import cotato.timetile.domain.user.domain.Role;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.global.common.Visibility;

public record UserProfileLoadResponse(
        Long id,
        String nickname,
        String profileImageUrl,
        Role role,
        Visibility visibility,
        int followingCount,
        int followerCount,
        int postCount,
        String introduction,
        @JsonInclude(Include.NON_NULL) Boolean isFollowing
) {
    public static UserProfileLoadResponse of(User user, String profileImageUrl, int postCount, Boolean isFollowing) {
        return new UserProfileLoadResponse(
                user.getId(),
                user.getNickname(),
                profileImageUrl,
                user.getRole(),
                user.getVisibility(),
                user.getFollowingCount(),
                user.getFollowerCount(),
                postCount,
                user.getIntroduction(),
                isFollowing
        );
    }
}

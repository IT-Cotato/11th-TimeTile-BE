package cotato.timetile.domain.profile.api.response;

import cotato.timetile.domain.user.domain.Role;
import cotato.timetile.domain.user.domain.User;

public record UserGradeResponse(
        String nickname,
        Role role,
        int visitCount,
        int postCount,
        int likeCount,
        int commentCount,
        double achievementRate
) {
    public static UserGradeResponse of(User user) {
        return new UserGradeResponse(
                user.getNickname(),
                user.getRole(),
                user.getVisitCount(),
                user.getPosts().size(),
                user.getPostLikes().size(),
                user.getComments().size(),
                Role.calculateAchieveRate(user)
        );
    }
}

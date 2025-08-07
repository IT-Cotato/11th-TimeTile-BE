package cotato.timetile.domain.search.api.dto;

import cotato.timetile.domain.user.domain.User;

public record MainSearchUserDto(
        String nickname,
        String introduction,
        String imageUrl
) {
    public static MainSearchUserDto of(User user, String imageUrl) {
        return new MainSearchUserDto(
                user.getNickname(),
                user.getIntroduction(),
                imageUrl
        );
    }
}

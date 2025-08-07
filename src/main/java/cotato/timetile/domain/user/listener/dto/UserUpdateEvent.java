package cotato.timetile.domain.user.listener.dto;

import cotato.timetile.domain.user.domain.User;

public record UserUpdateEvent(
        Long id
) {
    public static UserUpdateEvent of(User user) {
        return new UserUpdateEvent(user.getId());
    }
}

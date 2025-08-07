package cotato.timetile.domain.user.listener.dto;

import cotato.timetile.domain.user.domain.User;

public record UserCreationEvent(
        Long id
) {
    public static UserCreationEvent of(User user) {
        return new UserCreationEvent(user.getId());
    }
}

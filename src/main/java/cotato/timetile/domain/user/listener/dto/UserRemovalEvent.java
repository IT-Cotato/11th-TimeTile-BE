package cotato.timetile.domain.user.listener.dto;

import cotato.timetile.domain.user.domain.User;

public record UserRemovalEvent(
        String id
) {
    public static UserRemovalEvent of(User user) {
        return new UserRemovalEvent(user.getId().toString());
    }
}

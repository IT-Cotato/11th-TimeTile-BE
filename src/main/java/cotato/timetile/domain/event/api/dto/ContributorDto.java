package cotato.timetile.domain.event.api.dto;

import cotato.timetile.domain.user.domain.User;

public record ContributorDto(
        Long id,
        String nickname,
        String profileImageUrl
) {
    public static ContributorDto of(User participant, String profileImageUrl) {
        return new ContributorDto(
                participant.getId(),
                participant.getNickname(),
                profileImageUrl
        );
    }
}

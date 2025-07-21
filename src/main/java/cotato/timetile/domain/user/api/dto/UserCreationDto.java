package cotato.timetile.domain.user.api.dto;

import cotato.timetile.domain.user.api.request.LocalSignupRequest;
import cotato.timetile.domain.user.api.request.OAuth2SignupRequest;
import cotato.timetile.domain.user.domain.AuthProvider;

public record UserCreationDto(
        String email,
        String password,
        String nickname,
        String introduction,
        String imageKey,
        AuthProvider provider,
        String providerId
) {
    public static UserCreationDto of(LocalSignupRequest request) {
        return new UserCreationDto(
                request.email(),
                request.password(),
                request.nickname(),
                request.introduction(),
                request.imageKey(),
                AuthProvider.LOCAL,
                null
        );
    }

    public static UserCreationDto of(OAuth2SignupRequest request, TemporaryTokenPayloadDto dto) {
        return new UserCreationDto(
                dto.email(),
                null,
                request.nickname(),
                request.introduction(),
                request.imageKey(),
                dto.provider(),
                dto.providerId()
        );
    }
}

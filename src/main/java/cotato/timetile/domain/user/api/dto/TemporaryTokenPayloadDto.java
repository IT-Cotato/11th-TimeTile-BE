package cotato.timetile.domain.user.api.dto;

import cotato.timetile.domain.user.domain.AuthProvider;

public record TemporaryTokenPayloadDto(
        String email,
        AuthProvider provider,
        String providerId
) {
    public static TemporaryTokenPayloadDto of(String email, AuthProvider provider, String providerId) {
        return new TemporaryTokenPayloadDto(email, provider, providerId);
    }
}

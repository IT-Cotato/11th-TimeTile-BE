package cotato.timetile.auth.jwt;

import jakarta.validation.constraints.NotBlank;

public record AccessTokenReissueRequest(
        @NotBlank String refreshToken
) {
}

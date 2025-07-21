package cotato.timetile.domain.user.api.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoadFileUrlRequest(
        @NotBlank String extension
) {
}

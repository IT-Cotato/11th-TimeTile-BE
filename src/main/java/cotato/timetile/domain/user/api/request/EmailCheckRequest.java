package cotato.timetile.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailCheckRequest(
        @Email @NotBlank String email
) {
}

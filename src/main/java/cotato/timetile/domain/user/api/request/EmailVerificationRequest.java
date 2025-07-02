package cotato.timetile.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailVerificationRequest(
        @Email @NotBlank String email,
        @Pattern(regexp = "\\d{6}")
        @NotBlank String verificationCode
) {
}

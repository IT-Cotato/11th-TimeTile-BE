package cotato.timetile.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LocalLoginRequest(
        @Email @NotBlank String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>?,./\\\\|`~\\-])[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;\"'<>?,./\\\\|`~\\-]{6,19}$")
        @NotBlank String password
) {
}

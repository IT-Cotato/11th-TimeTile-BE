package cotato.timetile.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record LocalSignupRequest(
        @Email @NotBlank String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>?,./\\\\|`~\\-])[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;\"'<>?,./\\\\|`~\\-]{6,19}$")
        @NotBlank String password,
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$")
        @NotBlank String nickname,
        String introduction,
        String imageKey,
        List<Long> agreementIds
) {
}

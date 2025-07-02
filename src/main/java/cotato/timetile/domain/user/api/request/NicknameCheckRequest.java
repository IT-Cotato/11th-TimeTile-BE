package cotato.timetile.domain.user.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NicknameCheckRequest(
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$")
        @NotBlank String nickname
) {
}

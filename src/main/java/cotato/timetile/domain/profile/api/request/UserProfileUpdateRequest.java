package cotato.timetile.domain.profile.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$")
        @NotBlank
        String nickname,
        @Size(max = 30) String introduction,
        String imageKey
) {
}

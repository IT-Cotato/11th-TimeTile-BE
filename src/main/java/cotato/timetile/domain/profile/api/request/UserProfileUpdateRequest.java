package cotato.timetile.domain.profile.api.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @Pattern(regexp = "^[a-z가-힣]{2,15}$")
        String nickname,
        @Size(max = 30) String introduction,
        String imageKey
) {
}

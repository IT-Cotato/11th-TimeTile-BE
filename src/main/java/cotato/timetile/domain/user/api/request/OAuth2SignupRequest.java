package cotato.timetile.domain.user.api.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record OAuth2SignupRequest(
        @NotBlank String temporaryToken,
        @NotBlank String nickname,
        String introduction,
        String imageKey,
        List<Long> agreementIds
) {
}

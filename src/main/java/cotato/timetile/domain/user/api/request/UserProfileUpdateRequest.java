package cotato.timetile.domain.user.api.request;

import cotato.timetile.annotation.Image;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UserProfileUpdateRequest(
        @Image MultipartFile profileImage,
        @Pattern(regexp = "^[a-z가-힣]{2,15}$")
        String nickname,
        @Size(max = 30) String introduction
) {
}

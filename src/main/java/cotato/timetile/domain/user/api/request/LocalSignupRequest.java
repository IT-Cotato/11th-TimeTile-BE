package cotato.timetile.domain.user.api.request;

import cotato.timetile.domain.user.domain.AuthProvider;
import cotato.timetile.domain.user.domain.Role;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.global.common.Visibility;
import cotato.timetile.global.util.EncryptUtil;
import cotato.timetile.global.util.NicknameGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LocalSignupRequest(
        @Email @NotBlank String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>?,./\\\\|`~\\-])[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;\"'<>?,./\\\\|`~\\-]{6,19}$")
        @NotBlank String password
) {
    public User toUser() {
        return User.builder()
                .email(email)
                .password(EncryptUtil.encrypt(password))
                .nickname(NicknameGenerator.generateNickname())
                .provider(AuthProvider.LOCAL)
                .role(Role.WATCHER)
                .visibility(Visibility.PUBLIC)
                .build();
    }
}

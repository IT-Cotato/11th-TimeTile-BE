package cotato.timetile.domain.user.domain;

import cotato.timetile.global.exception.AuthenticationProviderException;
import java.util.Arrays;

public enum AuthProvider {
    LOCAL, GOOGLE, KAKAO;

    public static AuthProvider from(String name) {
        return Arrays.stream(values())
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(AuthenticationProviderException::invalid);
    }
}

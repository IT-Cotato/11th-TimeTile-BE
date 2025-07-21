package cotato.timetile.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import cotato.timetile.global.exception.AuthenticationProviderException;
import java.util.Arrays;

public enum AuthProvider {
    LOCAL, GOOGLE, KAKAO;

    @JsonCreator
    public static AuthProvider from(String name) {
        return Arrays.stream(values())
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(AuthenticationProviderException::invalid);
    }
}

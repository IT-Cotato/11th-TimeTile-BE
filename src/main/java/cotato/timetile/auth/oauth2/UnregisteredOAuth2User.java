package cotato.timetile.auth.oauth2;

import cotato.timetile.domain.user.domain.AuthProvider;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@AllArgsConstructor
@Getter
public class UnregisteredOAuth2User implements OAuth2User {

    private String providerId;
    private AuthProvider provider;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public static UnregisteredOAuth2User of(OAuth2UserInfo oAuth2UserInfo, AuthProvider provider) {
        return new UnregisteredOAuth2User(oAuth2UserInfo.getId(), provider, oAuth2UserInfo.getEmail(), null, null);
    }

    @Override
    public String getName() {
        return providerId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

}

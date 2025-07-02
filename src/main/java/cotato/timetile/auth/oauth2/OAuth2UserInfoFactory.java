package cotato.timetile.auth.oauth2;

import cotato.timetile.domain.user.domain.AuthProvider;
import cotato.timetile.global.exception.AuthenticationProviderException;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        if (authProvider.equals(AuthProvider.GOOGLE)) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (authProvider.equals(AuthProvider.KAKAO)) {
            return new KakaoOAuth2UserInfo(attributes);
        } else {
            throw AuthenticationProviderException.invalid();
        }
    }
    
}

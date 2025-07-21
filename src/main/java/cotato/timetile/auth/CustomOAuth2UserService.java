package cotato.timetile.auth;

import cotato.timetile.auth.oauth2.OAuth2UserInfo;
import cotato.timetile.auth.oauth2.OAuth2UserInfoFactory;
import cotato.timetile.auth.oauth2.UnregisteredOAuth2User;
import cotato.timetile.domain.user.domain.AuthProvider;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        AuthProvider provider = AuthProvider.from(userRequest.getClientRegistration().getRegistrationId());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        Optional<User> user = userRepository.findByProviderAndProviderId(provider, oAuth2UserInfo.getId());
        return user
                .<OAuth2User>map(UserPrincipal::of)
                .orElseGet(() -> UnregisteredOAuth2User.of(oAuth2UserInfo, provider));
    }

}

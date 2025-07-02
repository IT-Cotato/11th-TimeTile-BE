package cotato.timetile.auth;

import cotato.timetile.auth.oauth2.OAuth2UserInfo;
import cotato.timetile.auth.oauth2.OAuth2UserInfoFactory;
import cotato.timetile.domain.user.domain.AuthProvider;
import cotato.timetile.domain.user.domain.Role;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.util.NicknameGenerator;
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
        return processUser(userRequest, oAuth2User);
    }

    private OAuth2User processUser(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        AuthProvider provider = AuthProvider.from(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        String providerId = oAuth2UserInfo.getId();
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> registerUser(provider, providerId));
        return UserPrincipal.of(user);
    }

    private User registerUser(AuthProvider provider, String providerId) {
        User user = User.builder()
                .nickname(NicknameGenerator.generateNickname())
                .provider(provider)
                .providerId(providerId)
                .role(Role.WATCHER)
                .build();
        return userRepository.save(user);
    }

}

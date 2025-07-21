package cotato.timetile.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.auth.jwt.JwtProvider;
import cotato.timetile.domain.user.api.response.LoginResponse;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.properties.FrontendProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final FrontendProperties frontendProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UnregisteredOAuth2User unregisteredOAuth2User) {
            String temporaryToken = jwtProvider.generateTemporaryToken(unregisteredOAuth2User);
            response.sendRedirect(frontendProperties.registerUrl() + temporaryToken);
            response.setStatus(HttpServletResponse.SC_OK);
        } else if (principal instanceof UserPrincipal oAuth2User) {
            Long userId = oAuth2User.getId();
            String refreshToken = jwtProvider.generateRefreshToken(userId);
            String accessToken = jwtProvider.generateAccessToken(refreshToken);
            response.sendRedirect(frontendProperties.homeUrl());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.getWriter().write(objectMapper.writeValueAsString(CommonResponse.success(
                    SuccessResponse.OK, LoginResponse.of(accessToken, refreshToken))));
        }

    }

}

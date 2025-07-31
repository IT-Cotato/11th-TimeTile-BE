package cotato.timetile.auth.oauth2;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.auth.jwt.JwtProvider;
import cotato.timetile.global.properties.FrontendProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final FrontendProperties frontendProperties;
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
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(JwtProvider.ACCESS_TOKEN_VALID_DURATION)
                    .sameSite("None")
                    .build();
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(JwtProvider.REFRESH_TOKEN_VALID_DURATION)
                    .sameSite("None")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            response.sendRedirect(frontendProperties.homeUrl());
        }
    }

}

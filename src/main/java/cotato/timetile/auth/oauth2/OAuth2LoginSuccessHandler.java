package cotato.timetile.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.auth.jwt.JwtProvider;
import cotato.timetile.domain.user.api.response.LocalSignupResponse;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserPrincipal oAuth2User = (UserPrincipal) authentication.getPrincipal();
        Long userId = oAuth2User.getId();
        String refreshToken = jwtProvider.generateRefreshToken(userId);
        String accessToken = jwtProvider.generateAccessToken(refreshToken);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.getWriter().write(objectMapper.writeValueAsString(CommonResponse.success(
                SuccessResponse.OK, LocalSignupResponse.of(accessToken, refreshToken))));
    }
    
}

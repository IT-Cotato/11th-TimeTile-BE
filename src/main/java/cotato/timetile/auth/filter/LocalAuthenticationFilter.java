package cotato.timetile.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.timetile.auth.CustomLoginFailureHandler;
import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.auth.jwt.JwtProvider;
import cotato.timetile.domain.user.api.request.LocalLoginRequest;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.properties.FrontendProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LocalAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String FILTER_PROCESSES_URL = "/api/auth/login";
    private final FrontendProperties frontendProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtProvider jwtProvider;

    public LocalAuthenticationFilter(AuthenticationManager authenticationManager,
                                     CustomLoginFailureHandler customLoginFailureHandler,
                                     JwtProvider jwtProvider,
                                     FrontendProperties frontendProperties) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.frontendProperties = frontendProperties;
        setFilterProcessesUrl(FILTER_PROCESSES_URL);
        setAuthenticationFailureHandler(customLoginFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try (InputStream is = request.getInputStream()) {
            LocalLoginRequest dto = objectMapper.readValue(is, LocalLoginRequest.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    dto.email(), dto.password()
            );
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw UnauthorizedException.failed();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        UserPrincipal userDetails = (UserPrincipal) authResult.getPrincipal();
        String refreshToken = jwtProvider.generateRefreshToken(userDetails.getId());
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
    }

}

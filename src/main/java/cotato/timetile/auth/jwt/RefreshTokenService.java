package cotato.timetile.auth.jwt;

import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public AccessTokenReissueResponse reissue(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(UnauthorizedException::invalid);
        String accessToken = jwtProvider.generateAccessToken(token.getRefreshToken());
        return AccessTokenReissueResponse.of(accessToken);
    }

}

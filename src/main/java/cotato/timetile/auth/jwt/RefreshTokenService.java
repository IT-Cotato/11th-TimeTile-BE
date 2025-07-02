package cotato.timetile.auth.jwt;

import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public AccessTokenReissueResponse reissue(AccessTokenReissueRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(request.refreshToken())
                .orElseThrow(UnauthorizedException::invalid);
        String accessToken = jwtProvider.generateAccessToken(refreshToken.getRefreshToken());
        return AccessTokenReissueResponse.of(accessToken);
    }

}

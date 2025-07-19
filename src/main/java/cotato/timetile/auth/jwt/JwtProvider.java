package cotato.timetile.auth.jwt;

import cotato.timetile.domain.user.domain.Role;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final Duration REFRESH_TOKEN_VALID_DURATION = Duration.ofDays(14);
    private static final Duration ACCESS_TOKEN_VALID_DURATION = Duration.ofHours(3);
    private static final String JWT_TYPE = "JWT";
    private static final String CLAIM_ROLE = "role";
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public String generateRefreshToken(Long userId) {
        String refreshToken = generateToken(REFRESH_TOKEN_VALID_DURATION, userId, null);
        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        existingToken -> existingToken.update(refreshToken),
                        () -> refreshTokenRepository.save(new RefreshToken(userId, refreshToken))
                );
        return refreshToken;
    }

    @Transactional(readOnly = true)
    public String generateAccessToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            Long userId = refreshTokenRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(UnauthorizedException::invalid)
                    .getUserId();
            Role role = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed).getRole();
            return generateToken(ACCESS_TOKEN_VALID_DURATION, userId, role);
        } else {
            throw UnauthorizedException.invalid();
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(KeyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(KeyGenerator.getKeyFromString(jwtProperties.secretKey()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException | UnsupportedJwtException |
                 ExpiredJwtException e) {
            throw UnauthorizedException.invalid();
        }
    }

    private String generateToken(Duration validDuration, Long userId, Role role) {
        Date now = new Date();
        return Jwts.builder()
                .header().type(JWT_TYPE).and()
                .issuedAt(now)
                .subject(userId.toString())
                .claim(CLAIM_ROLE, role)
                .expiration(new Date(now.getTime() + validDuration.toMillis()))
                .signWith(KeyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .compact();
    }

}

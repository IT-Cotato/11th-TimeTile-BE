package cotato.timetile.domain.artist.api.dto;

public record SpotifyTokenDto(
        String access_token,
        String token_type,
        String expires_in
) {
}

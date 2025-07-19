package cotato.timetile.domain.artist.api.dto;

public record SpotifyArtistInfoDto(
        String id,
        String name,
        String imageUrl
) {
    public static SpotifyArtistInfoDto of(String id, String name, String imageUrl) {
        return new SpotifyArtistInfoDto(id, name, imageUrl);
    }
}

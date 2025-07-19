package cotato.timetile.domain.artist.api.request;

import jakarta.validation.constraints.NotBlank;

public record ArtistUpdateRequest(
        @NotBlank String spotifyIds
) {
}

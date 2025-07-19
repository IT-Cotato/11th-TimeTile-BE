package cotato.timetile.global.handler;

import com.fasterxml.jackson.databind.JsonNode;
import cotato.timetile.domain.artist.api.dto.SpotifyArtistInfoDto;
import cotato.timetile.domain.artist.api.dto.SpotifyTokenDto;
import cotato.timetile.global.properties.SpotifyProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SpotifyHandler {

    private final SpotifyProperties spotifyProperties;

    public List<SpotifyArtistInfoDto> getArtistInfo(String spotifyIds) {
        String GET_ARTIST_URL = "https://api.spotify.com/v1/artists";
        return getAccessToken()
                .flatMap(accessToken ->
                        WebClient.create(GET_ARTIST_URL)
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                        .queryParam("ids", spotifyIds)
                                        .build()
                                )
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.access_token())
                                .retrieve()
                                .bodyToMono(JsonNode.class)
                                .map(json -> {
                                    JsonNode artists = json.get("artists");
                                    List<SpotifyArtistInfoDto> artistInfo = new ArrayList<>();
                                    artists.forEach(artist -> {
                                        String id = artist.get("id").asText();
                                        String name = artist.get("name").asText();
                                        String imageUrl = artist.get("images").get(0).get("url").asText();
                                        artistInfo.add(SpotifyArtistInfoDto.of(id, name, imageUrl));
                                    });
                                    return artistInfo;
                                })
                ).block();
    }

    private Mono<SpotifyTokenDto> getAccessToken() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", spotifyProperties.clientId());
        body.add("client_secret", spotifyProperties.clientSecret());
        String AUTH_URL = "https://accounts.spotify.com/api/token";
        return WebClient.create(AUTH_URL)
                .post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(SpotifyTokenDto.class);
    }

}

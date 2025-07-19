package cotato.timetile.domain.artist.api;

import cotato.timetile.domain.artist.api.request.ArtistUpdateRequest;
import cotato.timetile.domain.artist.application.ArtistUpdateService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/artists")
@RequiredArgsConstructor
@Tag(name = "아티스트")
public class ArtistUpdateController {

    private final ArtistUpdateService artistUpdateService;

    @PutMapping
    @PreAuthorize(value = "hasRole('ADMIN')")
    @Operation(summary = "업데이트")
    public ResponseEntity<CommonResponse<?>> updateArtist(@Valid @RequestBody ArtistUpdateRequest request) {
        artistUpdateService.updateArtist(request);
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}

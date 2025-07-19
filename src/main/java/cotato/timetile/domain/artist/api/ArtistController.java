package cotato.timetile.domain.artist.api;

import cotato.timetile.domain.artist.application.ArtistLoadService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/artists")
@RequiredArgsConstructor
@Tag(name = "아티스트")
public class ArtistController {

    private final ArtistLoadService artistLoadService;

    @GetMapping(value = "/{artistId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "정보 조회")
    public ResponseEntity<CommonResponse<?>> load(@PathVariable String artistId) {
        return ApiResponseUtil.success(SuccessResponse.OK, artistLoadService.load(artistId));
    }

}

package cotato.timetile.domain.artist.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.artist.application.ArtistFollowService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/artists/{artistId}/follow")
@RequiredArgsConstructor
@Tag(name = "아티스트")
public class ArtistFollowController {

    private final ArtistFollowService artistFollowService;

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "팔로우")
    public ResponseEntity<CommonResponse<?>> follow(@PathVariable String artistId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        artistFollowService.follow(artistId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @DeleteMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "언팔로우")
    public ResponseEntity<CommonResponse<?>> unfollow(@PathVariable String artistId,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        artistFollowService.unfollow(artistId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "팔로우 상태 조회")
    public ResponseEntity<CommonResponse<?>> loadFollowStatus(@PathVariable String artistId,
                                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                artistFollowService.loadFollowStatus(artistId, userPrincipal.getId()));
    }

}

package cotato.timetile.domain.home.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.home.application.ArtistHomeService;
import cotato.timetile.domain.home.application.EventHomeService;
import cotato.timetile.domain.home.application.FollowHomeService;
import cotato.timetile.domain.home.application.PostHomeService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SortBy;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/home")
@RequiredArgsConstructor
@Tag(name = "홈")
public class HomeController {

    private final ArtistHomeService artistHomeService;
    private final EventHomeService eventHomeService;
    private final PostHomeService postHomeService;
    private final FollowHomeService followHomeService;

    @GetMapping(value = "/artists")
    @Operation(summary = "아티스트 조회")
    public ResponseEntity<CommonResponse<?>> loadArtists() {
        return ApiResponseUtil.success(SuccessResponse.OK, artistHomeService.load());
    }

    @GetMapping(value = "/events")
    @Operation(summary = "이벤트 조회")
    public ResponseEntity<CommonResponse<?>> loadEvents(
            @RequestParam(required = false, defaultValue = "HOTTEST") SortBy sortBy) {
        return ApiResponseUtil.success(SuccessResponse.OK, eventHomeService.load(sortBy));
    }

    @GetMapping(value = "/posts")
    @Operation(summary = "개별 기록 조회")
    public ResponseEntity<CommonResponse<?>> loadPosts(
            @RequestParam(required = false, defaultValue = "HOTTEST") SortBy sortBy) {
        return ApiResponseUtil.success(SuccessResponse.OK, postHomeService.load(sortBy));
    }

    @GetMapping(value = "/following-artists/events")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "팔로잉한 아티스트의 새로운 이벤트 조회")
    public ResponseEntity<CommonResponse<?>> loadNewEvents(@RequestParam(defaultValue = "1") int page,
                                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                followHomeService.loadNewEvents(page, userPrincipal.getId()));
    }

    @GetMapping(value = "/following-users/posts")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "팔로잉한 유저의 새로운 개별 기록 조회")
    public ResponseEntity<CommonResponse<?>> loadNewPosts(@RequestParam(defaultValue = "1") int page,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK, followHomeService.loadNewPosts(page, userPrincipal.getId()));
    }

}

package cotato.timetile.domain.search.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.search.application.RecentSearchService;
import cotato.timetile.domain.search.application.SearchService;
import cotato.timetile.domain.search.application.SuggestionSearchService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.NumberParam;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/search")
@RequiredArgsConstructor
@Tag(name = "검색")
public class SearchController {

    private final RecentSearchService recentSearchService;
    private final SuggestionSearchService suggestionSearchService;
    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "검색")
    public ResponseEntity<CommonResponse<?>> search(@RequestParam String query,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        recentSearchService.add(query, userPrincipal.getId());
        suggestionSearchService.add(query);
        return ApiResponseUtil.success(SuccessResponse.OK, searchService.searchAll(query));
    }

    @GetMapping(value = "/recent")
    @Operation(summary = "최근 검색어 조회")
    public ResponseEntity<CommonResponse<?>> loadRecent(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK, recentSearchService.load(userPrincipal.getId()));
    }

    @DeleteMapping(value = "/recent/{query}")
    @Operation(summary = "최근 검색어 선택 삭제")
    public ResponseEntity<CommonResponse<?>> removeRecent(@PathVariable String query,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        recentSearchService.remove(query, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @DeleteMapping(value = "/recent")
    @Operation(summary = "최근 검색어 전체 삭제")
    public ResponseEntity<CommonResponse<?>> clearRecent(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        recentSearchService.clear(userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @GetMapping(value = "/suggestions")
    @Operation(summary = "검색어 자동 완성")
    public ResponseEntity<CommonResponse<?>> loadSuggestion(@RequestParam String prefix) {
        return ApiResponseUtil.success(SuccessResponse.OK, suggestionSearchService.load(prefix));
    }

    @GetMapping(value = "/artists")
    @Operation(summary = "아티스트 검색")
    public ResponseEntity<CommonResponse<?>> searchArtists(@RequestParam(required = false, defaultValue = "1") int page,
                                                           @RequestParam String query) {
        return ApiResponseUtil.success(SuccessResponse.OK, searchService.searchArtists(page, query));
    }

    @GetMapping(value = "/posts")
    @Operation(summary = "개별 기록 검색")
    public ResponseEntity<CommonResponse<?>> searchPosts(@RequestParam(required = false, defaultValue = "1") int page,
                                                         @RequestParam String query) {
        return ApiResponseUtil.success(SuccessResponse.OK, searchService.searchPosts(page, query));
    }

    @GetMapping(value = "/posts/images")
    @Operation(summary = "개별 기록 이미지 모아 보기 검색")
    public ResponseEntity<CommonResponse<?>> searchPostImages(
            @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastPostId,
            @RequestParam String query) {
        return ApiResponseUtil.success(SuccessResponse.OK, searchService.searchPostImages(lastPostId, query));
    }

    @GetMapping(value = "/events")
    @Operation(summary = "이벤트 검색")
    public ResponseEntity<CommonResponse<?>> searchEvents(@RequestParam(required = false, defaultValue = "1") int page,
                                                          @RequestParam String query) {
        return ApiResponseUtil.success(SuccessResponse.OK, searchService.searchEvents(page, query));
    }

    @GetMapping(value = "/users")
    @Operation(summary = "유저 검색")
    public ResponseEntity<CommonResponse<?>> searchUsers(@RequestParam(required = false, defaultValue = "1") int page,
                                                         @RequestParam String query) {
        return ApiResponseUtil.success(SuccessResponse.OK, searchService.searchUsers(page, query));
    }

}

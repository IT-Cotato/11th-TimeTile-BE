package cotato.timetile.domain.search.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.search.application.RecentSearchService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/search")
@RequiredArgsConstructor
@Tag(name = "검색")
public class SearchController {

    private final RecentSearchService recentSearchService;

    @PostMapping
    public ResponseEntity<CommonResponse<?>> search(@RequestParam String query,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        recentSearchService.add(query, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @GetMapping(value = "/recent")
    public ResponseEntity<CommonResponse<?>> loadRecent(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK, recentSearchService.load(userPrincipal.getId()));
    }

    @DeleteMapping(value = "/recent/{query}")
    public ResponseEntity<CommonResponse<?>> removeRecent(@PathVariable String query,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        recentSearchService.remove(query, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @DeleteMapping(value = "/recent")
    public ResponseEntity<CommonResponse<?>> clearRecent(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        recentSearchService.clear(userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}

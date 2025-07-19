package cotato.timetile.domain.scrap.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.scrap.api.request.ScrapRequest;
import cotato.timetile.domain.scrap.application.ScrapService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/posts/{postId}/scrap")
@RequiredArgsConstructor
@Tag(name = "스크랩")
public class ScrapController {

    private final ScrapService scrapService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "스크랩 상태 조회")
    public ResponseEntity<CommonResponse<?>> loadScrapStatus(@PathVariable Long postId,
                                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK, scrapService.loadScrapStatus(postId, userPrincipal.getId()));
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "스크랩")
    public ResponseEntity<CommonResponse<?>> scrap(@RequestBody ScrapRequest request,
                                                   @PathVariable Long postId,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        scrapService.scrap(request, postId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

}

package cotato.timetile.domain.scrap.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.scrap.api.request.ScrapFolderCreationRequest;
import cotato.timetile.domain.scrap.api.request.ScrapFolderUpdateRequest;
import cotato.timetile.domain.scrap.application.ScrapFolderCreationService;
import cotato.timetile.domain.scrap.application.ScrapFolderLoadService;
import cotato.timetile.domain.scrap.application.ScrapFolderRemovalService;
import cotato.timetile.domain.scrap.application.ScrapFolderUpdateService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/scrap-folders")
@RequiredArgsConstructor
@Tag(name = "스크랩 폴더")
public class ScrapFolderController {

    private final ScrapFolderLoadService scrapFolderLoadService;
    private final ScrapFolderCreationService scrapFolderCreationService;
    private final ScrapFolderUpdateService scrapFolderUpdateService;
    private final ScrapFolderRemovalService scrapFolderRemovalService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "전체 조회")
    public ResponseEntity<CommonResponse<?>> loadAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK, scrapFolderLoadService.loadAll(userPrincipal.getId()));
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "생성")
    public ResponseEntity<CommonResponse<?>> create(@Valid @RequestBody ScrapFolderCreationRequest request,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        scrapFolderCreationService.create(request, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @PutMapping(value = "/{scrapFolderId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "수정")
    public ResponseEntity<CommonResponse<?>> update(@Valid @RequestBody ScrapFolderUpdateRequest request,
                                                    @PathVariable Long scrapFolderId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        scrapFolderUpdateService.update(request, scrapFolderId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @DeleteMapping(value = "/{scrapFolderId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "삭제")
    public ResponseEntity<CommonResponse<?>> remove(@PathVariable Long scrapFolderId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        scrapFolderRemovalService.remove(scrapFolderId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}
